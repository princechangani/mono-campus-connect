package com.monocampusconnect.dto;

import java.util.Date;
import lombok.Data;

@Data
public class ProfileRequest {
    private String firstName;
    private String lastName;
    private String department;
    private String semester;
    private String enrollmentNumber;
    private String facultyId;
    private String phoneNumber;
    private String email;
    private String address;
    private Date dateOfBirth;
    private String profilePictureUrl;


}
