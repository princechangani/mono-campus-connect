package com.monocampusconnect.dto;

import com.monocampusconnect.model.OtpToken;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerifyRequest {

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;

    private OtpToken.OtpPurpose purpose;

    // Required for PASSWORD_RESET purpose
    private String newPassword;
}

