package com.example.nutra.controller;

import com.example.nutra.dtos.SignupRequest;
import com.example.nutra.model.User;
import com.example.nutra.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3084" })
public class AdminController {

    private final AuthService authService;

    @PostMapping("/users/admin")
    public ResponseEntity<User> createAdmin(@RequestBody SignupRequest request) {
        return new ResponseEntity<>(authService.createAdmin(request), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    /**
     * Deactivates a user account. The user will no longer be able to log in.
     * Requires ADMIN role.
     *
     * PATCH /api/admin/users/{id}/deactivate
     */
    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(authService.deactivateUser(id));
    }

    /**
     * Re-activates a previously deactivated user account.
     * Requires ADMIN role.
     *
     * PATCH /api/admin/users/{id}/activate
     */
    @PatchMapping("/users/{id}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(authService.activateUser(id));
    }

    /**
     * Permanently deletes a user account from the database.
     * This action is irreversible. Use deactivate if you want a reversible option.
     * Requires ADMIN role.
     *
     * DELETE /api/admin/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
