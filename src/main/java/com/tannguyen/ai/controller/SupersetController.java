package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.SupersetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/superset")
@AllArgsConstructor
public class SupersetController {

    private final SupersetService supersetService;

    @GetMapping
    public ResponseEntity<?> getGuestToken(@RequestParam String dashboardId,
                                           @RequestParam Long analyticsConfigId) {
        return ResponseFactory.success(supersetService.getGuestToken(dashboardId, analyticsConfigId), "Get guest token successfully", HttpStatus.OK);
    }

}
