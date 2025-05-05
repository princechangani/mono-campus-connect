package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "result_details")
@Data
public class ResultDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "result_id")
    private Result result;

    private String subjectCode;
    private String subjectName;
    private double marksObtained;
    private double totalMarks;
    private String grade;
    private Date createdAt;
    private Date updatedAt;
}
