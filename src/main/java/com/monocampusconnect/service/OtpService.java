package com.monocampusconnect.service;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.OtpToken;
import com.monocampusconnect.model.User;
import com.monocampusconnect.repository.OtpTokenRepository;
import com.monocampusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OtpService {

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${otp.expiry.minutes:10}")
    private int otpExpiryMinutes;

    private static final SecureRandom RANDOM = new SecureRandom();

    // Rate Limiting: max 3 requests per 15 minutes per email
    private final Map<String, Queue<Instant>> requestHistory = new ConcurrentHashMap<>();

    private void checkRateLimit(String email) {
        Queue<Instant> history = requestHistory.computeIfAbsent(email, k -> new LinkedList<>());
        Instant now = Instant.now();
        
        // Remove requests older than 15 minutes
        while (!history.isEmpty() && history.peek().isBefore(now.minus(15, ChronoUnit.MINUTES))) {
            history.poll();
        }

        if (history.size() >= 3) {
            throw new ApiException("Too many OTP requests. Please try again after 15 minutes.", 429);
        }
        history.add(now);
    }

    /**
     * Generate a 6-digit OTP, store it, and send via email.
     */
    @Transactional
    public void sendOtp(String email, OtpToken.OtpPurpose purpose) {
        // For password reset, ensure user exists
        if (purpose == OtpToken.OtpPurpose.PASSWORD_RESET) {
            userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApiException("No account found with email: " + email, 404));
        }

        checkRateLimit(email);

        // Delete old OTPs for same email+purpose
        otpTokenRepository.deleteAllByEmailAndPurpose(email, purpose);

        String otp = generateOtp();

        OtpToken token = new OtpToken();
        token.setEmail(email);
        token.setOtp(otp);
        token.setPurpose(purpose);
        token.setUsed(false);
        token.setExpiresAt(new Date(System.currentTimeMillis() + otpExpiryMinutes * 60_000L));
        otpTokenRepository.save(token);

        emailService.sendOtpEmail(email, otp, purpose.name());
    }

    /**
     * Verify OTP. If valid, mark as used and return true.
     */
    @Transactional
    public void verifyOtp(String email, String otp, OtpToken.OtpPurpose purpose) {
        OtpToken token = otpTokenRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, purpose)
                .orElseThrow(() -> new ApiException("OTP not found or already used", 400));

        if (token.isExpired()) {
            throw new ApiException("OTP has expired. Please request a new one.", 400);
        }

        if (!token.getOtp().equals(otp)) {
            throw new ApiException("Invalid OTP", 400);
        }

        token.setUsed(true);
        otpTokenRepository.save(token);
    }

    /**
     * Verify OTP and reset password in one step.
     */
    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {
        verifyOtp(email, otp, OtpToken.OtpPurpose.PASSWORD_RESET);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", 404));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    private String generateOtp() {
        int otp = 100_000 + RANDOM.nextInt(900_000);
        return String.valueOf(otp);
    }
}

