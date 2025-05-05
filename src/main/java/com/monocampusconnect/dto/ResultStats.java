package com.monocampusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResultStats {
    private Long resultId;
    private String examName;
    private String courseName;
    private String studentName;
    private Double marksObtained;
    private Double totalMarks;
    private Double percentage;
    private LocalDateTime submissionDate;
    private String status;
}
