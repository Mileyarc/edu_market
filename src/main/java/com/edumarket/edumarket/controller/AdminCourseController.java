package com.edumarket.edumarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edumarket.edumarket.model.Course;
import com.edumarket.edumarket.model.Enrollment;
import com.edumarket.edumarket.repository.CourseRepository;
import com.edumarket.edumarket.repository.EnrollmentRepository;
import com.edumarket.edumarket.model.Category;
import com.edumarket.edumarket.repository.CategoryRepository;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/courses/";

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
                             @RequestParam(value = "newCategory", required = false) String newCategory,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
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
            try {
                Long categoryIdLong = Long.valueOf(categoryId);
                if (categoryIdLong != null) {
                    Optional<Category> catOpt = categoryRepository.findById(categoryIdLong);
            catOpt.ifPresent(course::setCategory);
                }
            } catch (NumberFormatException e) {
                result.rejectValue("category", "error.course", "Invalid category ID");
                return "admin/courses/form";
            }
        } else {
            result.rejectValue("category", "error.course", "Category is required");
            return "admin/courses/form";
        }

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                course.setImage("/images/courses/" + fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
                return "admin/courses/form";
            }
        }

        if (course != null) {
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("success", "Course created successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Course data is invalid");
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        if (id == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        model.addAttribute("formTitle", "Edit Course");
        model.addAttribute("actionUrl", "/admin/courses/edit/" + id);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/courses/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable("id") Long id,
                             @Valid @ModelAttribute Course course,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(value = "categoryId", required = false) String categoryId,
                             @RequestParam(value = "newCategory", required = false) String newCategory,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Course ID cannot be null");
            return "redirect:/admin/courses";
        }
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
            try {
                Long categoryIdLong = Long.valueOf(categoryId);
                if (categoryIdLong != null) {
                    Optional<Category> catOpt = categoryRepository.findById(categoryIdLong);
            catOpt.ifPresent(course::setCategory);
                }
            } catch (NumberFormatException e) {
                result.rejectValue("category", "error.course", "Invalid category ID");
                return "admin/courses/form";
            }
        } else {
            result.rejectValue("category", "error.course", "Category is required");
            return "admin/courses/form";
        }

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                course.setImage("/images/courses/" + fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
                return "admin/courses/form";
            }
        } else {
            // Keep existing image if no new image uploaded
            Optional<Course> existingCourseOpt = courseRepository.findById(id);
            if (existingCourseOpt.isPresent()) {
                Course existingCourse = existingCourseOpt.get();
                if (existingCourse.getImage() != null) {
                    course.setImage(existingCourse.getImage());
                }
            }
        }

        course.setId(id);
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Course ID cannot be null");
            return "redirect:/admin/courses";
        }
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        if (course != null) {
        courseRepository.delete(course);
        }
        redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/{id}/enrollers")
    public String viewEnrollers(@PathVariable("id") Long id, Model model) {
        if (id == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);
        return "admin/courses/enrollers";
    }

    @PostMapping("/{courseId}/enrollers/{enrollmentId}/approve")
    public String approveEnrollment(@PathVariable("courseId") Long courseId,
                                   @PathVariable("enrollmentId") Long enrollmentId,
                                   RedirectAttributes redirectAttributes) {
        if (courseId == null || enrollmentId == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid IDs");
            return "redirect:/admin/courses";
        }
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment Id:" + enrollmentId));
        enrollment.setStatus("APPROVED");
        enrollmentRepository.save(enrollment);
        redirectAttributes.addFlashAttribute("success", "Enrollment approved successfully!");
        return "redirect:/admin/courses/" + courseId + "/enrollers";
    }
} 