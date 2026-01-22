package com.edumarket.edumarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edumarket.edumarket.model.User;
import com.edumarket.edumarket.repository.UserRepository;

import jakarta.validation.Valid;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Password validation pattern
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$"
    );

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, 
                             BindingResult result,
                             @RequestParam("confirmPassword") String confirmPassword,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        // Validate password
        if (user.getPassword() == null || !PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            result.rejectValue("password", "error.password", 
                "Password must be between 8 and 20 characters, contain at least one number, one lowercase, one uppercase, one special character, and no whitespace");
            return "register";
        }

        // Check password confirmation
        if (!user.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.password", "Passwords do not match");
            return "register";
        }

        // Check if email already exists
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Email already exists");
            return "register";
        }

        // Encode password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepo.save(user);

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Login is handled by Spring Security, this method may not be needed
        return "redirect:/login";
    }
} 