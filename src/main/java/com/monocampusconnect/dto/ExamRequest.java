package com.monocampusconnect.dto;

import com.monocampusconnect.model.Exam;
import com.monocampusconnect.model.Exam.ExamType;
import lombok.Data;

import java.io.File;
import java.util.Date;
import java.util.List;

@Data
public class ExamRequest {
    private String examCode;
    private String courseCode;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private ExamType type;
    private List<String> enrolledStudents;


}
