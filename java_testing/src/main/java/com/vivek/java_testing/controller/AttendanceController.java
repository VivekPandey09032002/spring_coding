package com.vivek.java_testing.controller;

import com.vivek.java_testing.dto.RequestImage;
import com.vivek.java_testing.dto.ResponseAttendanceDetail;
import com.vivek.java_testing.dto.ResponseBody;
import com.vivek.java_testing.entity.Attendance;
import com.vivek.java_testing.entity.User;
import com.vivek.java_testing.exception.CustomException;
import com.vivek.java_testing.repository.AttendanceRepository;
import com.vivek.java_testing.repository.UserRepository;
import com.vivek.java_testing.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
@Log4j2
public class AttendanceController {
    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final AttendanceRepository attendanceRepository;
    private final ModelMapper mapper;
    private static final String USER_IN_TIME_ERROR = "cannot register in time ";
    private static final String USER_OUT_TIME_ERROR = "cannot register out time";

    @PostMapping("/in-time")
    public ResponseEntity<ResponseBody<Object>> registerUserInTime(@RequestBody RequestImage requestImage)
            throws IOException {
        final var email = requestImage.getEmail();
        log.debug("user email {}", email);
        User user = userRepository
                .findById(email)
                .orElseThrow(() -> new CustomException(USER_IN_TIME_ERROR,
                        List.of("no user found with email : " + email), HttpStatus.NOT_FOUND));
        // image data
        String data = requestImage.getData();
        String[] strings = data.split(",");
        // getting file extension or png default
        String extension = getExtension(strings);
        log.debug("file extension: {}", extension);
        // attendance already found
        if (attendanceRepository.getCurrentDayAttendance(user.getEmail(), LocalDate.now()).isPresent()) {
            throw new CustomException(USER_IN_TIME_ERROR, List.of("already entered in time for current day"),
                    HttpStatus.BAD_REQUEST);
        }
        // saving the user info the database
        Attendance currentDayAttendance = Attendance.builder()
                .attendanceDate(LocalDate.now())
                .inTime(LocalTime.now())
                .user(user)
                .build();
        attendanceRepository.save(currentDayAttendance);
        log.info("in time attendance saved");
        // convert base64 string to binary data and saving to classpath
        uploadService.saveImage(fileName(email, extension), strings[1]);
        log.info("image saved ");
        final var responseDto = mapper.map(currentDayAttendance, ResponseAttendanceDetail.class);
        final var responseBody = ResponseBody.builder().data(List.of(responseDto)).message("in time attendance saved")
                .status(HttpStatus.CREATED).build();
        return ResponseEntity.ok(responseBody);

    }

    @GetMapping("/out-time/{email}")
    public ResponseEntity<ResponseBody<Object>> registerUserOutTime(@PathVariable String email) throws IOException {
        log.debug("user email {}", email);

        userRepository.findById(email)
                .orElseThrow(() -> new CustomException(USER_OUT_TIME_ERROR, List.of("no user found with the given id"),
                        HttpStatus.BAD_REQUEST));

        Attendance attendance = attendanceRepository.getCurrentDayAttendance(email, LocalDate.now())
                .orElseThrow(() -> new CustomException(USER_OUT_TIME_ERROR, List.of("no in time found for the user"),
                        HttpStatus.BAD_REQUEST));
        if (attendance.getOutTime() != null) {
            throw new CustomException("cannot register out-time", List.of("already entered out time for current day "),
                    HttpStatus.BAD_REQUEST);
        }
        attendance.setOutTime(LocalTime.now());
        attendance = attendanceRepository.save(attendance);
        log.info("out time attendance saved");
        final var responseDto = mapper.map(attendance, ResponseAttendanceDetail.class);
        final var responseBody = ResponseBody.builder().data(List.of(responseDto)).message("out time attendance saved")
                .status(HttpStatus.CREATED).build();
        return ResponseEntity.ok(responseBody);

    }

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
}
