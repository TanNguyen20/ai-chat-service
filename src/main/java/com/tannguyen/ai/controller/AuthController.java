package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.AuthRequestDTO;
import com.tannguyen.ai.dto.response.AuthResponseDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.AuthService;
import com.tannguyen.ai.service.inf.TokenBlacklistService;
import com.tannguyen.ai.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/auth")
@AllArgsConstructor
public class AuthController {


    private AuthService authService;
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request);

        return ResponseFactory.success(response, "Login successfully", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO request) {
        authService.register(request.getUsername(), request.getPassword());
        return ResponseFactory.success(null, "User registered", HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = RequestUtil.extractTokenFromRequest(request);
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
        }
        return ResponseFactory.success(null, "Logged out successfully", HttpStatus.NO_CONTENT);
    }
}