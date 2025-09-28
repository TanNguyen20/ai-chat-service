package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.primary.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Page<Role> getRolesPagination(Pageable pageable);

    Role getRoleByName(RoleName roleName) throws NotFoundException;

    void createRole(RoleName roleName, String description);

    void deleteRole(RoleName roleName);
}
