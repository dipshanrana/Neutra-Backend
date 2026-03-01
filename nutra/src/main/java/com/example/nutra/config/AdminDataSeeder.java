package com.example.nutra.config;

import com.example.nutra.model.Admin;
import com.example.nutra.model.type.RoleType;
import com.example.nutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin already exists using the default static admin
        // email/username
        String adminUsername = "admin@admin.com";

        if (userRepository.findByUsername(adminUsername) == null) {
            Admin staticAdmin = new Admin();
            staticAdmin.setUsername(adminUsername);
            staticAdmin.setPassword(passwordEncoder.encode("admin123"));
            staticAdmin.setRole(RoleType.ADMIN);

            userRepository.save(staticAdmin);
            System.out.println("Static admin initialized: admin@admin.com / admin123");
        }
    }
}
