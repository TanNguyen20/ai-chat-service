package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.AnalyticsRequestDTO;
import com.tannguyen.ai.service.inf.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/analytics")
@AllArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<?> getAnalyticsList() {
        return ResponseEntity.ok(analyticsService.getAnalyticsList());
    }

    @PostMapping
    public ResponseEntity<?> createAnalytics(@RequestBody AnalyticsRequestDTO request) {
        analyticsService.addAnalytics(request);
        return ResponseEntity.ok("Create analytics successfully");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAnalytics(@PathVariable Long id) {
        analyticsService.deleteAnalytics(id);
        return ResponseEntity.ok("Delete analytics successfully");
    }
}
