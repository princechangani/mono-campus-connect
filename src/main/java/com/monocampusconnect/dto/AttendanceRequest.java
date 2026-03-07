package com.monocampusconnect.dto;

import com.monocampusconnect.model.Attendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AttendanceRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Course code is required")
    private String courseCode;

    @NotNull(message = "Session date is required")
    private Date sessionDate;

    @NotNull(message = "Status is required")
    private Attendance.AttendanceStatus status;

    private String remarks;
}
