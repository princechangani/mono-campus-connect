package com.monocampusconnect.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String department;
    private String semester;
    private String enrollmentNumber;
    private String facultyId;
    private Date dateOfBirth;
}
