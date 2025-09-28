package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.UserInfoRequestDTO;
import com.tannguyen.ai.dto.response.UserResponseDTO;
import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.repository.primary.UserRepository;
import com.tannguyen.ai.service.inf.RoleService;
import com.tannguyen.ai.service.inf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponseDTO::from);
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return UserResponseDTO.from(user);
    }

    @Override
    public void assignedRoleToUser(Long userId, List<RoleName> roleNames) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        try {
            for (RoleName roleName : roleNames) {
                Role role = roleService.getRoleByName(roleName);
                user.getRoles().add(role);
            }
            userRepository.save(user);
        } catch (NotFoundException ignored) {
            throw new NotFoundException("Role or User not found");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void resetUserInfo(Long userId, UserInfoRequestDTO userInfoRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if(userInfoRequestDTO.getIsEnabled() != null) {
            user.setEnabled(userInfoRequestDTO.getIsEnabled());
        }
        if(userInfoRequestDTO.getIsAccountNonLocked() != null) {
            user.setAccountNonLocked(userInfoRequestDTO.getIsAccountNonLocked());
        }
        if(userInfoRequestDTO.getIsAccountNonExpired() != null) {
            user.setAccountNonExpired(userInfoRequestDTO.getIsAccountNonExpired());
        }
        if(userInfoRequestDTO.getIsCredentialsNonExpired() != null ) {
            user.setCredentialsNonExpired(userInfoRequestDTO.getIsCredentialsNonExpired());
        }
        userRepository.save(user);
    }
}
