package com.edumarket.edumarket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE email = ?",
            Integer.class,
            "admin@edumarket.com"
        );
        if (count == null || count == 0 || count == -1) {
            String hashedPassword = passwordEncoder.encode("Admin@123"); // Change in production
            jdbcTemplate.update(
                "INSERT INTO users (email, password, full_name, age, phone_number, role, active) VALUES (?, ?, ?, ?, ?, ?, ?)",
                "admin@edumarket.com",
                hashedPassword,
                "Admin User",
                30,
                "1234567890",
                "ADMIN",
                true
            );
            System.out.println("Admin user created successfully");
        }
    }
}