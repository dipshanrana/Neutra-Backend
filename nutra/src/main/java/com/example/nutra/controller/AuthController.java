package com.example.nutra.controller;

import com.example.nutra.dtos.LoginRequest;
import com.example.nutra.dtos.LoginResponse;
import com.example.nutra.dtos.SignupRequest;
import com.example.nutra.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> adminLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.adminLogin(loginRequest));
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.userLogin(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(authService.signup(signupRequest));
    }

}
