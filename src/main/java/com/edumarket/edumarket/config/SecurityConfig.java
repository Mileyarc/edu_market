package com.edumarket.edumarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // Public access
                .requestMatchers("/courses", "/courses/**").permitAll() // Allow public viewing of courses
                .requestMatchers("/enroll/**", "/user/**").authenticated() // Enrollment requires login
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // Other requests need authentication
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .usernameParameter("username") // Spring Security uses 'username' by default, but we'll use email
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            )
            .csrf(csrf -> csrf.disable()); // Can enable CSRF for better security
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 