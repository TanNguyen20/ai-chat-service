package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.RoleAssignmentRequestDTO;
import com.tannguyen.ai.service.inf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<?> assignRoleToUser(
            @PathVariable Long userId,
            @RequestBody RoleAssignmentRequestDTO request) {
        userService.assignedRoleToUser(userId, request.getRoles());
        return ResponseEntity.ok("Role assigned successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
