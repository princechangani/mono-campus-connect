package com.monocampusconnect.dto;

import com.monocampusconnect.model.OtpToken;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    private OtpToken.OtpPurpose purpose;
}

