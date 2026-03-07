package com.monocampusconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Standalone config — PasswordEncoder has zero dependencies on SecurityConfig,
 * JwtAuthenticationFilter, or ProfileService. This breaks the circular reference:
 *   SecurityConfig → JwtAuthenticationFilter → ProfileService → PasswordEncoder → SecurityConfig
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
