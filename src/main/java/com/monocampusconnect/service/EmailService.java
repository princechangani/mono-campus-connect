package com.monocampusconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp, String purpose) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(getSubject(purpose));
            helper.setText(buildOtpEmailBody(otp, purpose), true);

            mailSender.send(message);
        } catch (Exception e) {
            // Log but don't fail the request — OTP is still saved in DB
            System.err.println("Failed to send OTP email to " + toEmail + ": " + e.getMessage());
        }
    }

    private String getSubject(String purpose) {
        return switch (purpose) {
            case "PASSWORD_RESET" -> "CampusConnect — Password Reset OTP";
            default -> "CampusConnect — Email Verification OTP";
        };
    }

    private String buildOtpEmailBody(String otp, String purpose) {
        String action = purpose.equals("PASSWORD_RESET") ? "reset your password" : "verify your email";
        return """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto; padding: 24px; border: 1px solid #e0e0e0; border-radius: 8px;">
                    <h2 style="color: #4F46E5;">CampusConnect</h2>
                    <p>Use the OTP below to <strong>%s</strong>. This code expires in <strong>10 minutes</strong>.</p>
                    <div style="font-size: 36px; font-weight: bold; letter-spacing: 8px; text-align: center; padding: 16px; background: #f3f4f6; border-radius: 8px; margin: 16px 0;">
                        %s
                    </div>
                    <p style="color: #6b7280; font-size: 13px;">If you did not request this, please ignore this email.</p>
                </div>
                """.formatted(action, otp);
    }
}

