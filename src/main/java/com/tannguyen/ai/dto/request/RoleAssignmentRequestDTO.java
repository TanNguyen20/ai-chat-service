package com.tannguyen.ai.dto.request;

import com.tannguyen.ai.enums.RoleName;
import lombok.Data;

import java.util.List;

@Data
public class RoleAssignmentRequestDTO {
    List<RoleName> roles;
}
