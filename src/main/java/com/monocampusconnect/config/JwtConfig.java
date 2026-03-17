package com.monocampusconnect.config;

import com.monocampusconnect.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtConfig {

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    // Strong, fixed secret key (base64-encoded, at least 256 bits)
    private static final String SECRET_KEY = "bXlTdXBlclNlY3VyZUp3dFNlY3JldEtleVRoYXRJc0xvbmdFbm91Z2hGb3JIbWFjU0hBMjU2QWxnb3JpdGht";

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List<?>) {
                return (List<String>) rolesObj;
            }
            String singleRole = claims.get("role", String.class);
            return singleRole != null ? List.of(singleRole) : List.of();
        });
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public UUID extractTenantId(String token) {
        String tenantIdStr = extractClaim(token, claims -> claims.get("tenantId", String.class));
        return tenantIdStr != null ? UUID.fromString(tenantIdStr) : null;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** Generate token embedding all roles from the user_role_mapping table */
    public String generateToken(User user, Collection<String> roleNames) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = roleNames == null || roleNames.isEmpty()
                ? List.of("USER")
                : new ArrayList<>(roleNames);

        claims.put("role", roles.get(0));
        claims.put("roles", roles);
        claims.put("userId", user.getUserId());
        if (user.getTenantId() != null) {
            claims.put("tenantId", user.getTenantId().toString());
        }

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /** Legacy overload kept for compatibility. */
    public String generateToken(User user) {
        return generateToken(user, List.of());
    }

    /** Legacy overload kept for compatibility — prefer generateToken(User, roles). */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (userDetails == null) return !isTokenExpired(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdmin(String token) {
        try {
            return extractRoles(token).stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdminOrFaculty(String token) {
        try {
            List<String> roles = extractRoles(token);
            return roles.contains("FACULTY") || roles.contains("ADMIN");
        } catch (Exception e) {
            return false;
        }
    }
}
