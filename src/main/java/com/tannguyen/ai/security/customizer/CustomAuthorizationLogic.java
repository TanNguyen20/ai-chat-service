package com.tannguyen.ai.security.customizer;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authz")
public class CustomAuthorizationLogic {
    public boolean isMinimalRole(Authentication authentication) {
        return !authentication.getAuthorities().isEmpty();
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }
}
