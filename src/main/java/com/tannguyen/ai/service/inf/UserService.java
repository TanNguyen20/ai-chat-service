package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.UserInfoRequestDTO;
import com.tannguyen.ai.dto.response.UserResponseDTO;
import com.tannguyen.ai.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponseDTO getCurrentUser();

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    void assignedRoleToUser(Long userId, List<RoleName> roleNames);

    void deleteUser(Long userId);

    void resetUserInfo(Long userId, UserInfoRequestDTO userInfoRequestDTO);
}
