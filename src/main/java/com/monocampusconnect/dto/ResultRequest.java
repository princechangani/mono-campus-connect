package com.monocampusconnect.dto;

import com.monocampusconnect.model.ResultDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResultRequest {
    private String studentId;
    private Long examId;
    private String courseCode;
    private List<ResultDetail> resultDetails;
    private double totalMarks;
    private double obtainedMarks;
    private String grade;
    private String status; // PENDING, PASSED, FAILED
    private String comments;
    private Date createdAt;
    private Date updatedAt;


}
