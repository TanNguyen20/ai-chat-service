package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.AuthRequestDTO;
import com.tannguyen.ai.dto.response.AuthResponseDTO;
import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.exception.ResourceAlreadyExistsException;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.repository.primary.RoleRepository;
import com.tannguyen.ai.repository.primary.UserRepository;
import com.tannguyen.ai.service.inf.AuthService;
import com.tannguyen.ai.service.inf.LoginAttemptService;
import com.tannguyen.ai.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        final String username = request.getUsername();

        // 1) If 30m lock window already expired, clear DB lock flag
        loginAttemptService.autoUnlockIfWindowExpired(username);

        try {
            // 2) Attempt authentication (will fail with LockedException if DB flag still locked)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );

            // 3) Success → reset attempts and ensure DB unlocked
            loginAttemptService.onAuthSuccess(username);

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user);
            return new AuthResponseDTO(token);

        } catch (LockedException e) {
            // Still locked (Redis key exists or DB says locked). Optional: include remaining time hint.
            throw e;

        } catch (BadCredentialsException e) {
            // 4) Bad creds → bump attempts and maybe lock
            loginAttemptService.onAuthFailure(username);

            long remaining = loginAttemptService.remainingAttempts(username);
            // Optional: wrap with your own exception that includes remaining attempts
            throw e;
        }
    }

    @Override
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
        // defaults: isAccountNonLocked=true (already set in your entity)
        userRepository.save(user);
    }
}
