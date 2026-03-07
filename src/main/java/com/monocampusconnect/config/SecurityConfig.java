package com.monocampusconnect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Lazy
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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

                    // Super Admin only endpoints
                    auth.requestMatchers("/api/tenants/**", "/api/super-admin/**")
                            .hasRole("SUPER_ADMIN");

                    // Admin-only management
                    auth.requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN");
                    auth.requestMatchers("/api/departments/**").hasAnyRole("ADMIN", "SUPER_ADMIN");
                    auth.requestMatchers("/api/timetable").hasAnyRole("ADMIN", "SUPER_ADMIN");
                    auth.requestMatchers("/api/timetable/**").hasAnyRole("ADMIN", "FACULTY", "STUDENT", "SUPER_ADMIN");

                    // Faculty + Admin
                    auth.requestMatchers("/api/exams/**").hasAnyRole("FACULTY", "ADMIN", "SUPER_ADMIN");
                    auth.requestMatchers("/api/materials/**").hasAnyRole("FACULTY", "ADMIN", "STUDENT", "SUPER_ADMIN");
                    auth.requestMatchers("/api/results/**").hasAnyRole("FACULTY", "ADMIN", "STUDENT", "SUPER_ADMIN");
                    auth.requestMatchers("/api/attendance/**").hasAnyRole("FACULTY", "ADMIN", "STUDENT", "SUPER_ADMIN");

                    // Notifications — all authenticated
                    auth.requestMatchers("/api/notifications/**").authenticated();

                    // Course endpoints — all authenticated
                    auth.requestMatchers("/api/courses/**").authenticated();

                    // Profile endpoints — all authenticated
                    auth.requestMatchers("/api/profile/**").authenticated();

                    // Event endpoints — all authenticated
                    auth.requestMatchers("/api/events/**").authenticated();

                    // All other requests require authentication
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept", "Origin", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
