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
@CrossOrigin(origins = "http://localhost:3000")
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
}
