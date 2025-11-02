package com.tannguyen.ai.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtil {

    @Value("${app.security.jwt-secret-key}")
    private String jwtSecretKeyRaw;

    @Value("${app.security.jwt-issuer:tannguyen.ai}")
    private String issuer;

    @Value("${app.security.jwt-expiration-ms:36000000}") // default 10h
    private long expirationMs;

    @Value("${app.security.jwt-allowed-clock-skew-seconds:60}")
    private long allowedClockSkewSeconds;

    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = tryBase64Decode(jwtSecretKeyRaw);
        if (keyBytes == null) keyBytes = jwtSecretKeyRaw.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 bytes (256 bits) for HS256.");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String generateToken(String subject) {
        return generateToken(subject, null, null);
    }

    public String generateToken(String subject, Map<String, Object> extraClaims, Long ttlOverrideMs) {
        long ttl = ttlOverrideMs != null ? ttlOverrideMs : this.expirationMs;
        Instant now = Instant.now();
        Instant exp = now.plusMillis(ttl);

        JwtBuilder builder = Jwts.builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp));

        if (extraClaims != null && !extraClaims.isEmpty()) {
            builder.claims(extraClaims);
        }

        return builder
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return Objects.equals(extractUsername(token), userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean validateTokenSubject(String token, String expectedSubject) {
        return Objects.equals(extractUsername(token), expectedSubject) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getPayload().getExpiration().before(new Date());
    }

    public long getRemainingMs(String token) {
        return parseToken(token).getPayload().getExpiration().getTime() - System.currentTimeMillis();
    }

    public Claims getAllClaims(String token) {
        return parseToken(token).getPayload();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .clockSkewSeconds(allowedClockSkewSeconds)
                .requireIssuer(issuer)
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);
    }

    private static byte[] tryBase64Decode(String maybeB64) {
        try {
            String t = maybeB64.trim();
            if (t.length() % 4 == 0) return Decoders.BASE64.decode(t);
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public long getExpirationFromToken(String token) {
        Claims claims = parseToken(token).getPayload();
        Date expiration = claims.getExpiration();

        if (expiration == null) {
            throw new IllegalStateException("JWT token does not contain an expiration claim");
        }

        return expiration.getTime() - System.currentTimeMillis();
    }
}
