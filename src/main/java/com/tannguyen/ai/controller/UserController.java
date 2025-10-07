package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.RoleAssignmentRequestDTO;
import com.tannguyen.ai.dto.request.UserInfoRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        return ResponseFactory.success(userService.getAllUsers(pageable), "Get all users successfully", HttpStatus.OK);
    }

    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> enableUser(@PathVariable Long id, @RequestBody UserInfoRequestDTO userInfoRequestDTO) {
        userService.resetUserInfo(id, userInfoRequestDTO);
        return ResponseFactory.success(null, "Update user successfully", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changeMyPassword(@Validated @RequestBody ChangePasswordRequestDTO req) {
        if (!req.getNewPassword().equals(req.getConfirmNewPassword())) {
            return ResponseFactory.error("New password and confirm password do not match", HttpStatus.BAD_REQUEST);
        }
        userService.changeMyPassword(req.getCurrentPassword(), req.getNewPassword());
        return ResponseFactory.success(null, "Password changed successfully", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> adminResetPassword(@PathVariable Long id,
                                                @Validated @RequestBody AdminResetPasswordRequestDTO req) {
        userService.adminResetPassword(id, req.getNewPassword());
        return ResponseFactory.success(null, "Password reset successfully", HttpStatus.NO_CONTENT);
    }
}
