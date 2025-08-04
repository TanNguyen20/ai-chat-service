package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.RoleRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/roles")
@AllArgsConstructor
@PreAuthorize("@authz.isAdmin(authentication)")
public class RoleController {

    private final RoleService roleService;

    @GetMapping()
    @PreAuthorize("@authz.isMinimalRole(authentication)")
    public ResponseEntity<?> getAllRoles() {
        return ResponseFactory.success(roleService.getAllRoles(), "Get all roles successfully", HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.createRole(roleRequestDTO.getName(), roleRequestDTO.getDescription());
        return ResponseFactory.success(null, "Create role successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.deleteRole(roleRequestDTO.getName());
        return ResponseFactory.success(null, "Delete role successfully", HttpStatus.NO_CONTENT);
    }
}
