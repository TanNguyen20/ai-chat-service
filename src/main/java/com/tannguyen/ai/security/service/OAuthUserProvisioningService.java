package com.tannguyen.ai.security.service;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.repository.primary.RoleRepository;
import com.tannguyen.ai.repository.primary.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthUserProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String PROVIDER = "azure";
    private static final boolean ATTACH_DEFAULT_ROLE = false;

    @Transactional
    public User ensureUserFromOidcAttributes(Map<String, Object> attrs) {
        // Common OIDC fields
        String sub = stringAttr(attrs, "sub");
        String email = firstNonBlank(
                stringAttr(attrs, "email"),
                stringAttr(attrs, "preferred_username")  // Azure often sets this
        );

        // Build a sensible full name
        String name = firstNonBlank(
                stringAttr(attrs, "name"),
                buildName(stringAttr(attrs, "given_name"), stringAttr(attrs, "family_name"))
        ).trim();

        String picture = stringAttr(attrs, "picture");

        if (email == null || email.isBlank()) {
            // Fallback to sub if email is absent (rare in Entra but possible if scope ‘email’ not granted)
            email = (sub != null ? sub : UUID.randomUUID().toString()) + "@login.microsoft.com";
        }
        String username = email; // choose your own policy; email is convenient & unique

        // Find by email first (preferred)
        Optional<User> existing = userRepository.findByEmail(email);
        User user = existing.orElseGet(User::new);
        boolean creating = (user.getId() == null);

        if (creating) {
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(name);
            // random password (never used for login; avoids null constraint)
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setRoles(new HashSet<>());

            if (ATTACH_DEFAULT_ROLE) {
                Optional<Role> role = roleRepository.findByName(RoleName.ROLE_USER);
                if (role.isPresent()) {
                    user.getRoles().add(role.get());
                }
            }
        } else {
            // Minimal profile refresh
            if (name != null && !name.isBlank()) {
                user.setFullName(name);
            }
        }

        user.setProvider(PROVIDER);
        user.setProviderSubject(sub);
        user.setAvatarUrl(picture);
        user.setLastLoginAt(Instant.now());

        User saved = userRepository.save(user);

        log.info("OAuth2 user provisioned: email={}, created={}", email, creating);
        return saved;
    }

    // Helpers
    private static String stringAttr(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? null : String.valueOf(v);
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }

    private static String buildName(String given, String family) {
        String g = (given == null) ? "" : given.trim();
        String f = (family == null) ? "" : family.trim();
        String joined = (g + " " + f).trim();
        return joined.isEmpty() ? null : joined;
    }
}
