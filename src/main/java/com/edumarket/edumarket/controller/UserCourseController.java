package com.edumarket.edumarket.controller;

import com.edumarket.edumarket.model.Course;
import com.edumarket.edumarket.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/courses")
public class UserCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public String listCourses(Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "6") int size,
                              @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage;

        if (search != null && !search.isEmpty()) {
            coursePage = courseRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            coursePage = courseRepository.findAll(pageable);
        }

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("search", search);

        return "user/courses/courses"; // Đảm bảo bạn có file: /templates/user/courses/list.html
    }
}
