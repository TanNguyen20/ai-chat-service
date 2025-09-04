package com.tannguyen.ai.repository.primary;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.model.primary.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);

    Set<Role> findAllByNameIn(List<RoleName> names);
}