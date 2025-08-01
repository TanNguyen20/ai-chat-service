package com.tannguyen.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardResponse<T> {
    private Instant timestamp;
    private String code;
    private String status;
    private String message;
    private String description;
    private T result;
}