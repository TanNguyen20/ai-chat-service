package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.repository.primary.RoleRepository;
import com.tannguyen.ai.service.inf.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByName(RoleName roleName) throws NotFoundException {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void createRole(RoleName roleName, String description) {
        Role role = new Role();
        role.setName(roleName);
        role.setDescription(description);

        roleRepository.save(role);
    }

    @Override
    public void deleteRole(RoleName roleName) {
        roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }
}
