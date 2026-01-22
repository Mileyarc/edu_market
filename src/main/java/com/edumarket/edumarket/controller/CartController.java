package com.edumarket.edumarket.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edumarket.edumarket.model.Course;
import com.edumarket.edumarket.model.CartItem;
import com.edumarket.edumarket.repository.CourseRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CourseRepository courseRepo;

    @GetMapping
    @SuppressWarnings("unchecked")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add/{id}")
    @SuppressWarnings("unchecked")
    public String addToCart(@PathVariable("id") Long id, HttpSession session) {
        if (id == null) {
            return "redirect:/courses";
        }
        Course course = courseRepo.findById(id).orElse(null);
        if (course == null) return "redirect:/courses";

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        // Check if course already in cart
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getCourse().getId().equals(id)) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(course, 1));
        }

        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }
}

