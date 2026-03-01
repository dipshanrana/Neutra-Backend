package com.example.nutra.security;

import com.example.nutra.dtos.LoginRequest;
import com.example.nutra.dtos.LoginResponse;
import com.example.nutra.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.nutra.repository.UserRepository;
import com.example.nutra.dtos.SignupRequest;
import com.example.nutra.model.Admin;
import com.example.nutra.model.Customer;
import com.example.nutra.model.type.RoleType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
        loginResponse.setJwtToken(token);
        return loginResponse;

    }

    public LoginResponse signup(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        User user;

        Customer customer = new Customer();
        customer.setUsername(signupRequest.getUsername());
        customer.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        customer.setRole(RoleType.CUSTOMER);
        user = customer;

        user = userRepository.save(user);

        String token = authUtil.generateAccessToken(user);
        LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
        loginResponse.setJwtToken(token);
        return loginResponse;
    }

    public User createAdmin(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(signupRequest.getUsername());
        admin.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        admin.setRole(RoleType.ADMIN);

        return userRepository.save(admin);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
