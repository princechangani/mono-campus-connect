package com.monocampusconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TimetableRequest {

    @NotBlank(message = "Day of week is required")
    private String dayOfWeek;

    @NotBlank(message = "Time slot is required")
    private String timeSlot;

    @NotBlank(message = "Course code is required")
    private String courseCode;

    private String courseName;

    private String facultyId;
    private String facultyName;
    private String roomNumber;

    @NotBlank(message = "Semester is required")
    private String semester;
}

