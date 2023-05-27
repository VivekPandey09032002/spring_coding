package com.vivek.java_testing.service;

import com.vivek.java_testing.entity.Attendance;
import com.vivek.java_testing.entity.User;
import com.vivek.java_testing.exception.CustomException;
import com.vivek.java_testing.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Log4j2
@Service
public class AttendanceServiceImpl implements  AttendanceService {
    private static final String USER_IN_TIME_ERROR = "Cannot register in time ";

    private final AttendanceRepository attendanceRepository;
    @Override
    public Attendance saveAttendance(User user) {
        //attendance for current date is found
        if (attendanceRepository.getCurrentDayAttendance(user.getUserId(), LocalDate.now()).isPresent()) {
            log.error("user already signed in");
            throw new CustomException(USER_IN_TIME_ERROR,
                    List.of("Already signed in for the day at " + LocalTime.now()
                            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US))),
                    HttpStatus.BAD_REQUEST);
        }
        // saving the user info the database
        Attendance currentDayAttendance = Attendance.builder().attendanceDate(LocalDate.now()).inTime(LocalTime.now()).user(user).build();
        attendanceRepository.save(currentDayAttendance);
        return currentDayAttendance;
    }
}
