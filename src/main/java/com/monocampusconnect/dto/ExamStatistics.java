package com.monocampusconnect.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ExamStatistics {
    private double averageMarks;
    private double highestMarks;
    private double lowestMarks;
    private double passRate;
    private int totalStudents;
}
