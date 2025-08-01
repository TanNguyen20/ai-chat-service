package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.model.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseFactory {

    public static <T> ResponseEntity<StandardResponse<T>> success(T data, String message, HttpStatus statusCode) {
        return ResponseEntity.ok(StandardResponse.<T>builder()
                .timestamp(Instant.now())
                .code(String.valueOf(statusCode.value()))
                .status(statusCode.name())
                .message(message)
                .result(data)
                .build());
    }

    public static ResponseEntity<StandardResponse<Void>> error(
            HttpStatus status, String message, String description) {
        return ResponseEntity.status(status).body(StandardResponse.<Void>builder()
                .timestamp(Instant.now())
                .code(String.valueOf(status.value()))
                .status(status.name())
                .message(message)
                .description(description)
                .result(null)
                .build());
    }
}