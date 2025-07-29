package com.tannguyen.ai.security.filter;

import com.tannguyen.ai.service.inf.TokenBlacklistService;
import com.tannguyen.ai.util.JwtUtil;
import com.tannguyen.ai.util.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Early exit if already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = RequestUtil.extractTokenFromRequest(request);

        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check blacklist
        if (blacklistService.isTokenBlacklisted(jwtToken)) {
            log.warn("Blocked blacklisted token from request: {}", jwtToken);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            return;
        }

        String username;
        try {
            username = jwtUtil.extractUsername(jwtToken);
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            return;
        }

        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Load user and validate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtUtil.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            log.warn("JWT token validation failed for user: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            return;
        }

        filterChain.doFilter(request, response);
    }
}