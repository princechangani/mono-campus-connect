package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "results")
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentId;
    private String examCode;
    private String courseCode;


    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    private List<ResultDetail> resultDetails;
    private double totalMarks;
    private double obtainedMarks;
    private String grade;
    private String status; 
    private String comments;
    private Date resultDate;
    private Date createdAt;
    private Date updatedAt;

  
}
