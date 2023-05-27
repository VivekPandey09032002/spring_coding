package com.vivek.java_testing.controller;

import com.vivek.java_testing.dto.request.RequestImage;
import com.vivek.java_testing.dto.response.ResponseAttendanceDetail;
import com.vivek.java_testing.dto.response.ResponseSingle;
import com.vivek.java_testing.entity.Attendance;
import com.vivek.java_testing.entity.User;
import com.vivek.java_testing.exception.CustomException;
import com.vivek.java_testing.repository.AttendanceRepository;
import com.vivek.java_testing.repository.UserRepository;
import com.vivek.java_testing.service.AttendanceService;
import com.vivek.java_testing.service.UploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
@Log4j2
public class AttendanceController {
    private static final String USER_IN_TIME_ERROR = "Cannot register in time ";
    private static final String USER_OUT_TIME_ERROR = "Cannot register out time";
    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final AttendanceRepository attendanceRepository;

    private final AttendanceService attendanceService;
    private final ModelMapper mapper;

    private static String fileName(String name, String extension) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return name + currentTimeStamp + "." + extension;
    }

    private static String getExtension(String[] strings) {
        String extension;
        switch (strings[0]) {// check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default:// should write cases for more images types
                extension = "jpg";
                break;
        }
        return extension;
    }

    @PostMapping("/in-time")
    public ResponseEntity<ResponseSingle<ResponseAttendanceDetail>> registerUserInTime(@RequestBody @Valid RequestImage requestImage,
                                                                                       BindingResult results,
                                                                                       HttpServletRequest request) throws IOException {
        //validating dto
        if (results.hasErrors()) {
            log.error("invalid admin dto");
            final var errors = results.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new CustomException(USER_IN_TIME_ERROR, errors, HttpStatus.BAD_REQUEST);
        }

        final var userId = requestImage.getUserId();
        log.debug("User userId {}", userId);
        //getting the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_IN_TIME_ERROR, List.of("No user found with userId : " + userId),
                        HttpStatus.NOT_FOUND));
        log.info("valid user found");
        // image data
        String data = requestImage.getData();
        String[] strings = data.split(",");
        // getting file extension or png default
        String extension = getExtension(strings);
        log.debug("file extension: {}", extension);
        // saving current day attendance
        Attendance currentDayAttendance = attendanceService.saveAttendance(user);
        log.info("in time attendance saved");
        // convert base64 string to binary data and saving to classpath
        uploadService.saveImage(fileName(userId, extension), strings[1]);
        log.info("image saved ");
        final var responseDto = mapper.map(currentDayAttendance, ResponseAttendanceDetail.class);
        final var responseBody = ResponseSingle.<ResponseAttendanceDetail>builder().data(responseDto).message("In time attendance saved").status(HttpStatus.CREATED).build();
        return ResponseEntity.ok(responseBody);
    }
    @GetMapping("/out-time/{userId}")
    public ResponseEntity<ResponseSingle<ResponseAttendanceDetail>> registerUserOutTime(@PathVariable String userId) throws IOException {
        log.debug("user userId {}", userId);

        //no user found with given id
        if (!userRepository.existsById(userId)) {
            throw new CustomException(USER_OUT_TIME_ERROR, List.of("No user found with the given id: " + userId), HttpStatus.BAD_REQUEST);
        }


        Attendance attendance = attendanceRepository.getCurrentDayAttendance(userId, LocalDate.now())
                .orElseThrow(() -> new CustomException(USER_OUT_TIME_ERROR,
                        List.of("No in time found for the userId: " + userId),
                        HttpStatus.BAD_REQUEST));
        if (attendance.getOutTime() != null) {
            throw new CustomException("Cannot register out-time",
                    List.of("Already signed out for the day " + LocalTime.now()
                            .format(DateTimeFormatter
                                    .ofLocalizedTime(FormatStyle.SHORT)
                                    .withLocale(Locale.US))),
                    HttpStatus.BAD_REQUEST);
        }
        //saving the out time attendance
        attendance.setOutTime(LocalTime.now());
        attendance = attendanceRepository.save(attendance);
        log.info("out time attendance saved");
        final var responseDto = mapper.map(attendance, ResponseAttendanceDetail.class);
        final var responseBody = ResponseSingle.<ResponseAttendanceDetail>builder()
                .data(responseDto).message("Out time attendance saved")
                .status(HttpStatus.CREATED)
                .build();
        return ResponseEntity.ok(responseBody);

    }
}
