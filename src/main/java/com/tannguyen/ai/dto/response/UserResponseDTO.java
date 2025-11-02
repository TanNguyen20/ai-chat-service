package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.model.primary.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private Set<Role> roles;
    private String fullName;
    private String email;
    private String avatarUrl;

    private LocalDateTime createdAt;

    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .isAccountNonExpired(user.isAccountNonExpired())
                .isAccountNonLocked(user.isAccountNonLocked())
                .isCredentialsNonExpired(user.isCredentialsNonExpired())
                .isEnabled(user.isEnabled())
                .roles(user.getRoles())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
