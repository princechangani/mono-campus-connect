package com.monocampusconnect.config;

import com.monocampusconnect.service.ProfileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Runs once per request. Validates the JWT, sets the SecurityContext,
 * and populates TenantContextHolder with the tenantId from the token.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ProfileService profileService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            if (!jwtConfig.isTokenValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String username = jwtConfig.extractUsername(token);
            final String role = jwtConfig.extractRole(token);
            final UUID tenantId = jwtConfig.extractTenantId(token);

            // Set tenant context for this request
            if (tenantId != null) {
                TenantContextHolder.setTenantId(tenantId);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Build authorities from all roles in the token
                List<String> roles = jwtConfig.extractRoles(token);
                var authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());
                // Fallback to single role claim if roles list is empty
                if (authorities.isEmpty()) {
                    String singleRole = jwtConfig.extractRole(token);
                    String authorityName = (singleRole != null) ? "ROLE_" + singleRole : "ROLE_USER";
                    authorities = List.of(new SimpleGrantedAuthority(authorityName));
                }

                UserDetails userDetails = profileService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            // Invalid token — clear context and continue (Spring Security will deny access)
            SecurityContextHolder.clearContext();
            TenantContextHolder.clear();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Always clean up ThreadLocal to prevent memory leaks
            TenantContextHolder.clear();
        }
    }
}

