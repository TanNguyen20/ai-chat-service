package com.tannguyen.ai.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tannguyen.ai.dto.response.SupersetGuestToken;
import com.tannguyen.ai.service.inf.SupersetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    private final RestTemplate restTemplate = new RestTemplate();

    public SupersetGuestToken getGuestToken() {
        // Step 1: Login to get access token
        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("username", username);
        loginPayload.put("password", password);
        loginPayload.put("provider", provider);
        loginPayload.put("refresh", true);

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> loginRequest = new HttpEntity<>(loginPayload, loginHeaders);
        ResponseEntity<Map> loginResponse = restTemplate.exchange(baseUrl + "/security/login", HttpMethod.POST, loginRequest, Map.class);

        String accessToken = (String) loginResponse.getBody().get("access_token");

        // Step 2: Request guest token
        Map<String, Object> user = Map.of(
                "username", "",
                "first_name", "",
                "last_name", ""
        );

        Map<String, Object> guestPayload = Map.of(
                "resources", List.of(Map.of("type", "dashboard", "id", "5661684e-6dbc-4029-9f64-a6534aa0e5b2")),
                "rls", List.of(),
                "user", user
        );

        HttpHeaders guestHeaders = new HttpHeaders();
        guestHeaders.setContentType(MediaType.APPLICATION_JSON);
        guestHeaders.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> guestRequest = new HttpEntity<>(guestPayload, guestHeaders);
        ResponseEntity<Map> guestResponse = restTemplate.exchange(baseUrl + "/security/guest_token/", HttpMethod.POST, guestRequest, Map.class);

        String guestToken = (String) guestResponse.getBody().get("token");
        SupersetGuestToken supersetGuestToken = new SupersetGuestToken();
        supersetGuestToken.setToken(guestToken);
        return supersetGuestToken;
    }
}
