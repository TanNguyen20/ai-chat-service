package com.tannguyen.ai.security.customizer;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("authz")
public class CustomAuthorizationLogic {
    public boolean isMinimalRole(MethodSecurityExpressionOperations root) {
        return !root.getAuthentication().getAuthorities().isEmpty();
    }
}
