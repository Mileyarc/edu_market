package com.edumarket.edumarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edumarket.edumarket.model.User;
import com.edumarket.edumarket.repository.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        
        if (search != null && !search.isEmpty()) {
            // Search by email or full name
            userPage = userRepository.findAll(pageable); // You might want to add custom search method
        } else {
            userPage = userRepository.findAll(pageable);
        }
        
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("search", search);
        return "admin/users/list";
    }

    @PostMapping("/{id}/activate")
    public String activateUser(@PathVariable("id") Long id, 
                              @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                              @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                              @RequestParam(value = "search", required = false) String search,
                              RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid user ID!");
            return "redirect:/admin/users";
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(true);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "User activated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found!");
        }
        String redirectUrl = "/admin/users?page=" + page + "&size=" + size;
        if (search != null && !search.isEmpty()) {
            redirectUrl += "&search=" + search;
        }
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivateUser(@PathVariable("id") Long id,
                                @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                @RequestParam(value = "search", required = false) String search,
                                RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid user ID!");
            return "redirect:/admin/users";
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "User deactivated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found!");
        }
        String redirectUrl = "/admin/users?page=" + page + "&size=" + size;
        if (search != null && !search.isEmpty()) {
            redirectUrl += "&search=" + search;
        }
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{id}")
    public String viewUserDetail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid user ID!");
            return "redirect:/admin/users";
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            return "admin/users/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found!");
            return "redirect:/admin/users";
        }
    }
}
