package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.exception.ResourceAlreadyExistsException;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.repository.primary.RoleRepository;
import com.tannguyen.ai.repository.primary.UserRepository;
import com.tannguyen.ai.service.inf.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String username, String password) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Default role not found"));

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResourceAlreadyExistsException(username + " already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }
}
