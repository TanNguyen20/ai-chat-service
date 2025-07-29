package com.tannguyen.ai.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.tannguyen.ai.constant.Secret.JWT_SECRET;

@Component
public class JwtUtil {

    private final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SIGNING_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return parseToken(token).getPayload().getExpiration().before(new Date());
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SIGNING_KEY)
                .build()
                .parseSignedClaims(token);
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
