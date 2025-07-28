package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.response.UserResponseDTO;
import com.tannguyen.ai.enums.RoleName;

import java.util.List;

public interface UserService {

    UserResponseDTO getCurrentUser();

    List<UserResponseDTO> getAllUsers();

    void assignedRoleToUser(Long userId, List<RoleName> roleNames);
}
