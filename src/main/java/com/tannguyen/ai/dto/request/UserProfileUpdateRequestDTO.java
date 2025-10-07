package com.tannguyen.ai.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateRequestDTO {

    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 255, message = "Full name is too long")
    private String fullname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is invalid")
    @Size(max = 255, message = "Email is too long")
    private String email;
}
