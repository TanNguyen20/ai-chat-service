package com.tannguyen.ai.dto.request;

import com.tannguyen.ai.enums.RoleName;
import lombok.Data;

@Data
public class RoleRequestDTO {
    private RoleName name;

    private String description;
}
