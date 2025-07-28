package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.RoleRequestDTO;
import com.tannguyen.ai.service.inf.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @GetMapping()
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/create")
    public void createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.createRole(roleRequestDTO.getName());
    }

    @PostMapping("/delete")
    public void deleteRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.deleteRole(roleRequestDTO.getName());
    }
}
