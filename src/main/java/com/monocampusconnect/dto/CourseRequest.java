package com.monocampusconnect.dto;

import lombok.Data;

@Data
public class CourseRequest {
    private String courseCode;
    private String courseName;
    private String department;
    private int credits;
    private String instructor;
    private String semester;


}
