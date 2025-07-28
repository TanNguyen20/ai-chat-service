package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Role getRoleByName(RoleName roleName) throws NotFoundException;

    void createRole(RoleName roleName);

    void deleteRole(RoleName roleName);
}
