package com.edumarket.edumarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.edumarket.edumarket.repository.CourseRepository;

@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;

    @GetMapping
    public String listCourses(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "6") int size,
                             @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<com.edumarket.edumarket.model.Course> coursePage;
        if (search != null && !search.isEmpty()) {
            coursePage = courseRepo.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            coursePage = courseRepo.findAll(pageable);
        }
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("search", search);
        return "user/courses/courses"; // View: courses.html
    }

    @PostMapping("/admin/courses/save")
    public String saveCourse(@ModelAttribute("course") com.edumarket.edumarket.model.Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Xử lý lỗi
            return "admin/courses/form";
        }
        // Lưu course
        return "redirect:/admin/courses";
    }

    @GetMapping("/{id}")
    public String viewCourseDetail(@PathVariable Long id, Model model) {
        com.edumarket.edumarket.model.Course course = courseRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "user/course-detail"; // View: user/course-detail.html
    }
}
