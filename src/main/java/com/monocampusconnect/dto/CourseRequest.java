package com.monocampusconnect.dto;

import com.monocampusconnect.model.Course;
import lombok.Data;

@Data
public class CourseRequest {
    private String courseCode;
    private String courseName;
    private String department;
    private int credits;
    private String instructor;
    private String facultyId;
    private String semester;
    private Course.SubjectType subjectType;
    private String category;
}
