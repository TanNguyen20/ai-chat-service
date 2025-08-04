package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.response.SupersetGuestTokenResponseDTO;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.AnalyticsConfig;
import com.tannguyen.ai.repository.AnalyticsConfigRepository;
import com.tannguyen.ai.service.inf.SupersetService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${superset.provider}")
    private String provider;

    private final RestTemplate restTemplate;

    private final CookieStore cookieStore;

    private final AnalyticsConfigRepository analyticsConfigRepository;

    @Autowired
    public SupersetServiceImpl(AnalyticsConfigRepository analyticsConfigRepository) {
        cookieStore = new BasicCookieStore();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(requestFactory);
        this.analyticsConfigRepository = analyticsConfigRepository;
    }

    public SupersetGuestTokenResponseDTO getGuestToken(String dashboardId, Long analyticsConfigId) {
        AnalyticsConfig analyticsConfig = analyticsConfigRepository.findById(analyticsConfigId)
                .orElseThrow(() -> new NotFoundException("Not found analytics config"));

        String analyticsHostname = analyticsConfig.getHostname();
        String accessToken = getAccessToken(analyticsConfig.getUsername(), analyticsConfig.getPassword(), analyticsHostname);
        String csrfToken = getCSRFToken(accessToken, analyticsHostname);

        Map<String, Object> user = Map.of(
                "username", "",
                "first_name", "",
                "last_name", ""
        );

        Map<String, Object> guestPayload = Map.of(
                "resources", List.of(Map.of("type", "dashboard", "id", dashboardId)),
                "rls", List.of(),
                "user", user
        );

        HttpHeaders guestHeaders = new HttpHeaders();
        guestHeaders.setContentType(MediaType.APPLICATION_JSON);
        guestHeaders.set("X-CSRFToken", csrfToken);
        guestHeaders.setBearerAuth(accessToken);
        // Add the Referer header which is required by Superset
        guestHeaders.set("Referer", analyticsHostname);

        HttpEntity<Map<String, Object>> guestRequest = new HttpEntity<>(guestPayload, guestHeaders);
        ResponseEntity<Map> guestResponse = restTemplate.exchange(analyticsHostname + "/api/v1/security/guest_token/", HttpMethod.POST, guestRequest, Map.class);

        Map<String, Object> responseBody = guestResponse.getBody();
        if (responseBody == null || !responseBody.containsKey("token")) {
            throw new RuntimeException("Invalid guest token response");
        }
        String guestToken = String.valueOf(responseBody.get("token"));
        SupersetGuestTokenResponseDTO supersetGuestTokenResponseDTO = new SupersetGuestTokenResponseDTO();
        supersetGuestTokenResponseDTO.setToken(guestToken);
        return supersetGuestTokenResponseDTO;
    }

    private String getCSRFToken(String accessToken, String hostname) {
        HttpHeaders csrfHeaders = new HttpHeaders();
        csrfHeaders.setBearerAuth(accessToken);
        csrfHeaders.setContentType(MediaType.APPLICATION_JSON);
        // Also add Referer header for CSRF token request
        csrfHeaders.set("Referer", hostname);

        HttpEntity<Void> csrfRequest = new HttpEntity<>(csrfHeaders);
        ResponseEntity<Map> response = restTemplate.exchange(hostname + "/api/v1/security/csrf_token/", HttpMethod.GET, csrfRequest, Map.class);

        return (String) response.getBody().get("result");
    }

    private String getAccessToken(String username, String password, String hostname) {
        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("username", username);
        loginPayload.put("password", password);
        loginPayload.put("provider", provider);
        loginPayload.put("refresh", true);

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        // Also add Referer header for login request
        loginHeaders.set("Referer", hostname);

        HttpEntity<Map<String, Object>> loginRequest = new HttpEntity<>(loginPayload, loginHeaders);
        ResponseEntity<Map> loginResponse = restTemplate.exchange(hostname + "/api/v1/security/login", HttpMethod.POST, loginRequest, Map.class);

        String accessToken = (String) loginResponse.getBody().get("access_token");
        if (token == null) {
            throw new RuntimeException("Failed to obtain access token");
        }
        return accessToken;
    }
}
