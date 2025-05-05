package com.monocampusconnect.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamQuestionDTO {
    private String question;
    private String type; // MCQ, SHORT_ANSWER, ESSAY
    private int marks;
    private List<String> options; // For MCQ
    private String correctAnswer;
}
