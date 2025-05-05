package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String examCode;
    private String courseCode;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private ExamType type;

    @ElementCollection
    private List<String> enrolledStudents;

    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Result> results;

    public enum ExamType {
        MIDTERM, FINAL, QUIZ, ASSIGNMENT
    }


}
