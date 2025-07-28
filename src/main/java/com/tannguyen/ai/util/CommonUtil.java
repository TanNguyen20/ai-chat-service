package com.tannguyen.ai.util;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

public class CommonUtil {
    public static String extractUiHost(HttpServletRequest request) {
        try {
            String uiHost = request.getHeader("Origin");
            if (uiHost == null || uiHost.isBlank()) {
                uiHost = request.getHeader("Referer");
            }

            if (uiHost == null || uiHost.isBlank()) {
                return null;
            }

            URI uri = new URI(uiHost);
            return uri.getHost();
        } catch (Exception e) {
            return null;
        }
    }
}
