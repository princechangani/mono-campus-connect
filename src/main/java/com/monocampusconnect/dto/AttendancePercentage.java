package com.monocampusconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendancePercentage {
    private Long studentId;
    private String courseCode;
    private long totalClasses;
    private long presentClasses;
    private double percentage;
    private boolean belowThreshold; // true if < 75%
}
