package com.tannguyen.ai.initializer;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.repository.primary.RoleRepository;
import com.tannguyen.ai.repository.primary.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            Role roleSuperAdminExisted = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN).orElse(null);
            Role roleUserExisted = roleRepository.findByName(RoleName.ROLE_USER).orElse(null);
            if (roleSuperAdminExisted == null && roleUserExisted == null) {
                Role roleAdmin = new Role();
                roleAdmin.setName(RoleName.ROLE_ADMIN);
                roleRepository.save(roleAdmin);

                Role roleUser = new Role();
                roleUser.setName(RoleName.ROLE_USER);
                roleRepository.save(roleUser);
            }

            User user = userRepository.findByUsername("superadmin").orElse(null);

            if (user == null) {
                Role adminRoleAdded = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null);

                User superAdminUser = new User();

                superAdminUser.setUsername("superadmin");
                superAdminUser.setPassword(passwordEncoder.encode("superadmin"));
                superAdminUser.setRoles(Set.of(adminRoleAdded));

                userRepository.save(superAdminUser);
            }
        };
    }
}
