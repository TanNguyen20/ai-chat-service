package com.tannguyen.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminResetPasswordRequestDTO {
    @NotBlank
    private String newPassword;
}
