package com.tannguyen.ai.dto;

import java.util.Map;

public record PageAccessDTO(
        Long id,
        String url,
        String description,
        Map<String, CrudSetDTO> rolePermissions // key = role name e.g. "ROLE_ADMIN"
) {}