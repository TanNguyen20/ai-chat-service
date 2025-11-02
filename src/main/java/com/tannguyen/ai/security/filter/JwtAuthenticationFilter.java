package com.tannguyen.ai.security.filter;

import com.tannguyen.ai.service.inf.TokenBlacklistService;
import com.tannguyen.ai.util.JwtUtil;
import com.tannguyen.ai.util.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // If you ever deliver JWT via cookie, keep this in sync with your success handler
    private static final String JWT_COOKIE_NAME = "ACCESS_TOKEN";

    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1) Try to read JWT from Authorization header first
        String jwtToken = RequestUtil.extractTokenFromRequest(request);

        // 1b) If not found, optionally read from cookie (supports cookie-based auth too)
        if (jwtToken == null) {
            jwtToken = readTokenFromCookie(request, JWT_COOKIE_NAME);
        }

        // 2) If no token, just continue; keep whatever Authentication Spring has set (e.g., OAuth2 session)
        if (jwtToken == null || jwtToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Block blacklisted tokens early
        if (blacklistService.isTokenBlacklisted(jwtToken)) {
            log.warn("Blocked blacklisted token from request");
            unauthorized(response);
            return;
        }

        // 4) Extract subject (username/email based on your JwtUtil policy)
        final String username;
        try {
            username = jwtUtil.extractUsername(jwtToken);
        } catch (Exception e) {
            log.warn("Failed to parse JWT: {}", e.getMessage());
            unauthorized(response);
            return;
        }
        if (username == null || username.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5) If the context already has *our* auth for the same user, skip re-auth
        Authentication existing = SecurityContextHolder.getContext().getAuthentication();
        if (existing instanceof UsernamePasswordAuthenticationToken existingUpat
                && existingUpat.getPrincipal() instanceof UserDetails existingDetails
                && username.equals(existingDetails.getUsername())) {
            // Already authenticated as the same app user; proceed
            filterChain.doFilter(request, response);
            return;
        }

        // 6) Load user & validate token; replace any existing Authentication (e.g., OAuth2AuthenticationToken)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtUtil.validateToken(jwtToken, userDetails)) {
            log.warn("JWT validation failed for user: {}", username);
            unauthorized(response);
            return;
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
    }

    @Nullable
    private String readTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (cookieName.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
