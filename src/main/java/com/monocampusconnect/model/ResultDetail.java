package com.monocampusconnect.model;

import lombok.Data;

import java.util.UUID;

@Data
public class ResultDetail {
    private Long resultDetailsId;
    private UUID resultDetailsPublicId;
    private String subjectCode;
    private String subjectName;
    private double marksObtained;
    private double totalMarks;
    private String grade;
}
