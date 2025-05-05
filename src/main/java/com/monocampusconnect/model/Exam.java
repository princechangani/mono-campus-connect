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
    
    @Column(nullable = false, unique = true)
    private String examCode;
    
    @Column(nullable = false)
    private String courseCode;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Date startDate;
    
    @Column(nullable = false)
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
