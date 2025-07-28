package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.response.UserResponseDTO;
import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.Role;
import com.tannguyen.ai.model.User;
import com.tannguyen.ai.repository.UserRepository;
import com.tannguyen.ai.service.inf.RoleService;
import com.tannguyen.ai.service.inf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDTO::from).toList();
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
}
