package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.SupersetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/superset")
@AllArgsConstructor
public class SupersetController {

    private final SupersetService supersetService;

    @GetMapping
    public ResponseEntity<?> getGuestToken() {
        return ResponseFactory.success(supersetService.getGuestToken(), "Get guest token successfully", HttpStatus.OK);
    }

}
