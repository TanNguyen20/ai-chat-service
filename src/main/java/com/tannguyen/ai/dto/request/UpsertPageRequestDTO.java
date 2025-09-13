package com.tannguyen.ai.dto.request;

import com.tannguyen.ai.dto.CrudSetDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record UpsertPageRequestDTO(
        @NotBlank @Size(max = 256) String url,
        @NotBlank @Size(max = 512) String description,
        Map<String, CrudSetDTO> rolePermissions
) {
}