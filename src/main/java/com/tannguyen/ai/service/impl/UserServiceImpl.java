package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.UserInfoRequestDTO;
import com.tannguyen.ai.dto.response.UserResponseDTO;
import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.exception.BadRequestException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public void changeMyPassword(String currentPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException("New password must be different from the current password");
        }

        validatePasswordPolicy(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optional: force re-login by expiring credentials if you track sessions/tokens
        // user.setCredentialsNonExpired(false);
    }

    @Override
    @Transactional
    public void adminResetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        validatePasswordPolicy(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        // usually you want to force the user to change password on next login
        user.setCredentialsNonExpired(false);
        userRepository.save(user);
    }

    private void validatePasswordPolicy(String pwd) {
        // Minimal example; replace with your real policy/validator.
        if (Objects.isNull(pwd) || pwd.length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
        // You could add checks for uppercase/lowercase/digits/special, breached lists, etc.
    }
}
