// src/main/java/com/tannguyen/ai/security/handler/OAuth2LoginSuccessHandler.java
package com.tannguyen.ai.security.handler;

import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.security.service.OAuthUserProvisioningService;
import com.tannguyen.ai.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component("oAuth2LoginSuccessHandler") // ensure this is the ONLY bean with this name
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final OAuthUserProvisioningService provisioning;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private static final String CALLBACK_PATH = "/auth/callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {
        if (!(auth instanceof OAuth2AuthenticationToken oauth)) {
            response.sendRedirect(frontendBaseUrl);
            return;
        }

        Map<String, Object> attrs = ((DefaultOidcUser) oauth.getPrincipal()).getAttributes();
        User user = provisioning.ensureUserFromOidcAttributes(attrs);
        if (!user.isEnabled()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is disabled. Contact an administrator.");
            return;
        }

        // IMPORTANT: subject must match what your JwtAuthenticationFilter / UserDetailsService uses
        String subject = user.getUsername(); // or user.getEmail() if that's your canonical username
        String jwt = jwtUtil.generateToken(subject);
        long expiresInSec = Math.max(1L, jwtUtil.getRemainingMs(jwt) / 1000L);

        String redirectUrl = UriComponentsBuilder
                .fromHttpUrl(frontendBaseUrl + CALLBACK_PATH)
                .fragment("access_token=" + jwt + "&expires_in=" + expiresInSec)
                .build(true)
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
