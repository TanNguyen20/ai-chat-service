package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.RoleAssignmentRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseFactory.success(userService.getAllUsers(), "Get all users successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<?> assignRoleToUser(
            @PathVariable Long userId,
            @RequestBody RoleAssignmentRequestDTO request) {
        userService.assignedRoleToUser(userId, request.getRoles());
        return ResponseFactory.success(null, "Role assigned successfully", HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        return ResponseFactory.success(userService.getCurrentUser(), "Get user info successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseFactory.success(null, "Delete user successfully", HttpStatus.NO_CONTENT);
    }
}
