package com.monocampusconnect.controller;

import com.monocampusconnect.dto.OtpRequest;
import com.monocampusconnect.dto.OtpVerifyRequest;
import com.monocampusconnect.model.OtpToken;
import com.monocampusconnect.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    /**
     * Send OTP to email.
     * POST /api/otp/send
     * Body: { "email": "user@example.com", "purpose": "EMAIL_VERIFICATION" | "PASSWORD_RESET" }
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody OtpRequest request) {
        OtpToken.OtpPurpose purpose = request.getPurpose() != null
                ? request.getPurpose()
                : OtpToken.OtpPurpose.EMAIL_VERIFICATION;

        otpService.sendOtp(request.getEmail(), purpose);
        return ResponseEntity.ok(Map.of(
                "message", "OTP sent to " + request.getEmail() + ". Valid for 10 minutes."
        ));
    }

    /**
     * Verify OTP.
     * POST /api/otp/verify
     * Body: { "email": "...", "otp": "123456", "purpose": "EMAIL_VERIFICATION" }
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        OtpToken.OtpPurpose purpose = request.getPurpose() != null
                ? request.getPurpose()
                : OtpToken.OtpPurpose.EMAIL_VERIFICATION;

        otpService.verifyOtp(request.getEmail(), request.getOtp(), purpose);
        return ResponseEntity.ok(Map.of("message", "OTP verified successfully."));
    }

    /**
     * Reset password using OTP.
     * POST /api/otp/reset-password
     * Body: { "email": "...", "otp": "123456", "purpose": "PASSWORD_RESET", "newPassword": "..." }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody OtpVerifyRequest request) {
        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password is required"));
        }
        otpService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset successfully. Please log in with your new password."));
    }

    /**
     * Resend OTP.
     * POST /api/otp/resend
     */
    @PostMapping("/resend")
    public ResponseEntity<Map<String, String>> resendOtp(@Valid @RequestBody OtpRequest request) {
        OtpToken.OtpPurpose purpose = request.getPurpose() != null
                ? request.getPurpose()
                : OtpToken.OtpPurpose.EMAIL_VERIFICATION;

        otpService.sendOtp(request.getEmail(), purpose);
        return ResponseEntity.ok(Map.of("message", "OTP resent to " + request.getEmail()));
    }
}

