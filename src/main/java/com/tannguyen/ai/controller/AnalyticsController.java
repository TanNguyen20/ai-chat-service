package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.AnalyticsRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseFactory.success(analyticsService.getAnalyticsList(), "Get analytics list successfully", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAnalyticsItemInfo(@PathVariable Long id) {
        return ResponseFactory.success(analyticsService.getAnalyticsById(id), "Get analytics list successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAnalytics(@RequestBody AnalyticsRequestDTO request) {
        analyticsService.addAnalytics(request);
        return ResponseFactory.success(null, "Create analytics successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAnalytics(@PathVariable Long id) {
        analyticsService.deleteAnalytics(id);
        return ResponseFactory.success(null, "Delete analytics successfully", HttpStatus.NO_CONTENT);
    }
}
