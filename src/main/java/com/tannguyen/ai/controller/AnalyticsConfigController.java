package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.AnalyticsConfigRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.AnalyticsConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + "/analytics-config")
public class AnalyticsConfigController {
    private AnalyticsConfigService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnalyticsConfigRequestDTO requestDTO) {
        service.create(requestDTO);
        return ResponseFactory.success(null, "Create analytics config successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseFactory.success(service.getById(id), "Get analytics config successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseFactory.success(service.getAll(), "Get all analytics config successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AnalyticsConfigRequestDTO requestDTO) {
        service.update(id, requestDTO);
        return ResponseFactory.success(null, "Update analytics config successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseFactory.success(null, "Delete analytics config successfully", HttpStatus.NO_CONTENT);
    }
}
