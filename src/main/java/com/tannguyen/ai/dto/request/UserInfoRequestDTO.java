package com.tannguyen.ai.dto.request;

import lombok.Data;

@Data
public class UserInfoRequestDTO {

    private Boolean isAccountNonExpired;

    private Boolean isAccountNonLocked;

    private Boolean isCredentialsNonExpired;

    private Boolean isEnabled;
}
