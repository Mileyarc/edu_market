package com.edumarket.edumarket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to EduMarket! <a href='/login'>Login</a> or <a href='/register'>Register</a>";
    }
} 