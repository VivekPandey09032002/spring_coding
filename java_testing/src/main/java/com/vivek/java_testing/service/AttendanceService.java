package com.vivek.java_testing.service;

import com.vivek.java_testing.entity.Attendance;
import com.vivek.java_testing.entity.User;

public interface AttendanceService {
    Attendance saveAttendance(User user);
}
