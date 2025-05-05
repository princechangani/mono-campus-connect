package com.monocampusconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/auth/**",
            "/api/otp/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/docs/**"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    // Public endpoints
                    auth.requestMatchers(PUBLIC_ENDPOINTS.toArray(String[]::new)).permitAll();

                    // Course endpoints - accessible to all authenticated users
                    auth.requestMatchers(
                            "/api/courses/**"
                    ).authenticated();

                    // Admin-only endpoints
                    auth.requestMatchers(
                            "/api/admin/**",
                            "/api/exams/type/**",
                            "/api/materials/type/**",
                            "/api/results/status/**"
                    ).hasRole("ADMIN");

                    // Faculty-only endpoints
                    auth.requestMatchers(
                            "/api/exams/**",
                            "/api/materials/**",
                            "/api/results/**"
                    ).hasAnyRole("FACULTY", "ADMIN");

                    // Student-only endpoints
                    auth.requestMatchers(
                            "/api/exams/student/**",
                            "/api/materials/course/**",
                            "/api/results/student/**"
                    ).hasAnyRole("STUDENT", "FACULTY", "ADMIN");

                    // Profile endpoints
                    auth.requestMatchers(
                            "/api/profile/**"
                    ).hasAnyRole("STUDENT", "FACULTY", "ADMIN");

                    // All other authenticated requests
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174", "http://localhost:5175"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept", "Origin", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
