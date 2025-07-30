package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.response.SupersetGuestToken;
import com.tannguyen.ai.service.inf.SupersetService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupersetServiceImpl implements SupersetService {

    @Value("${superset.base-url}")
    private String baseUrl;

    @Value("${superset.username}")
    private String username;

    @Value("${superset.password}")
    private String password;

    @Value("${superset.provider}")
    private String provider;

    @Value("${superset.referrer:#{baseUrl}}")
    private String referrer;

    private final RestTemplate restTemplate;

    private final CookieStore cookieStore;

    public SupersetServiceImpl() {
        cookieStore = new BasicCookieStore();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    public SupersetGuestToken getGuestToken() {
        String accessToken = getAccessToken();
        String csrfToken = getCSRFToken(accessToken);

        Map<String, Object> user = Map.of(
                "username", "",
                "first_name", "",
                "last_name", ""
        );

        Map<String, Object> guestPayload = Map.of(
                "resources", List.of(Map.of("type", "dashboard", "id", "74c5a97f-71fc-4330-96a0-7af644a70f83")),
                "rls", List.of(),
                "user", user
        );

        HttpHeaders guestHeaders = new HttpHeaders();
        guestHeaders.setContentType(MediaType.APPLICATION_JSON);
        guestHeaders.set("X-CSRFToken", csrfToken);
        guestHeaders.setBearerAuth(accessToken);
        // Add the Referer header which is required by Superset
        guestHeaders.set("Referer", referrer);

        HttpEntity<Map<String, Object>> guestRequest = new HttpEntity<>(guestPayload, guestHeaders);
        ResponseEntity<Map> guestResponse = restTemplate.exchange(baseUrl + "/security/guest_token/", HttpMethod.POST, guestRequest, Map.class);

        String guestToken = (String) guestResponse.getBody().get("token");
        SupersetGuestToken supersetGuestToken = new SupersetGuestToken();
        supersetGuestToken.setToken(guestToken);
        return supersetGuestToken;
    }

    private String getCSRFToken(String accessToken) {
        HttpHeaders csrfHeaders = new HttpHeaders();
        csrfHeaders.setBearerAuth(accessToken);
        csrfHeaders.setContentType(MediaType.APPLICATION_JSON);
        // Also add Referer header for CSRF token request
        csrfHeaders.set("Referer", referrer);

        HttpEntity<Void> csrfRequest = new HttpEntity<>(csrfHeaders);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/security/csrf_token/", HttpMethod.GET, csrfRequest, Map.class);

        return (String) response.getBody().get("result");
    }

    private String getAccessToken() {
        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("username", username);
        loginPayload.put("password", password);
        loginPayload.put("provider", provider);
        loginPayload.put("refresh", true);

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        // Also add Referer header for login request
        loginHeaders.set("Referer", referrer);

        HttpEntity<Map<String, Object>> loginRequest = new HttpEntity<>(loginPayload, loginHeaders);
        ResponseEntity<Map> loginResponse = restTemplate.exchange(baseUrl + "/security/login", HttpMethod.POST, loginRequest, Map.class);

        return (String) loginResponse.getBody().get("access_token");
    }
}