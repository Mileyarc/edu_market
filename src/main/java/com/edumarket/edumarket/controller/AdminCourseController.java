package com.edumarket.edumarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edumarket.edumarket.model.Course;
import com.edumarket.edumarket.repository.CourseRepository;
import com.edumarket.edumarket.model.Category;
import com.edumarket.edumarket.repository.CategoryRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listCourses(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
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
        return "admin/courses/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("formTitle", "Create Course");
        model.addAttribute("actionUrl", "/admin/courses/create");
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/courses/form";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute Course course,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(value = "categoryId", required = false) String categoryId,
                             @RequestParam(value = "newCategory", required = false) String newCategory) {
        if (result.hasErrors()) {
            return "admin/courses/form";
        }
        if ("add_new".equals(categoryId) && newCategory != null && !newCategory.trim().isEmpty()) {
            Category cat = categoryRepository.findByName(newCategory.trim())
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName(newCategory.trim());
                    return categoryRepository.save(c);
                });
            course.setCategory(cat);
        } else if (categoryId != null && !"add_new".equals(categoryId)) {
            Optional<Category> catOpt = categoryRepository.findById(Long.valueOf(categoryId));
            catOpt.ifPresent(course::setCategory);
        } else {
            result.rejectValue("category", "error.course", "Category is required");
            return "admin/courses/form";
        }
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("success", "Course created successfully!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        model.addAttribute("formTitle", "Edit Course");
        model.addAttribute("actionUrl", "/admin/courses/edit/" + id);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/courses/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                             @Valid @ModelAttribute Course course,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(value = "categoryId", required = false) String categoryId,
                             @RequestParam(value = "newCategory", required = false) String newCategory) {
        if (result.hasErrors()) {
            return "admin/courses/form";
        }
        if ("add_new".equals(categoryId) && newCategory != null && !newCategory.trim().isEmpty()) {
            Category cat = categoryRepository.findByName(newCategory.trim())
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName(newCategory.trim());
                    return categoryRepository.save(c);
                });
            course.setCategory(cat);
        } else if (categoryId != null && !"add_new".equals(categoryId)) {
            Optional<Category> catOpt = categoryRepository.findById(Long.valueOf(categoryId));
            catOpt.ifPresent(course::setCategory);
        } else {
            result.rejectValue("category", "error.course", "Category is required");
            return "admin/courses/form";
        }
        course.setId(id);
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        courseRepository.delete(course);
        redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        return "redirect:/admin/courses";
    }
} 