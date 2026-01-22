package com.edumarket.edumarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edumarket.edumarket.model.Enrollment;
import com.edumarket.edumarket.model.User;
import com.edumarket.edumarket.model.Course;
import com.edumarket.edumarket.repository.EnrollmentRepository;
import com.edumarket.edumarket.repository.UserRepository;
import com.edumarket.edumarket.repository.CourseRepository;

import java.util.Optional;

@Controller
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public String enrollInCourse(@RequestParam("courseId") Long courseId, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found. Please login again.");
            return "redirect:/login";
        }

        User user = userOpt.get();
        if (!user.getActive()) {
            redirectAttributes.addFlashAttribute("error", "Your account is inactive. Please contact administrator.");
            return "redirect:/courses";
        }

        if (courseId == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid course ID.");
            return "redirect:/courses";
        }
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Course not found.");
            return "redirect:/courses";
        }

        Course course = courseOpt.get();

        // Check if already enrolled
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserAndCourse(user, course);
        if (existingEnrollment.isPresent()) {
            redirectAttributes.addFlashAttribute("info", "You are already enrolled in this course.");
            return "redirect:/courses/" + courseId;
        }

        // Create new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setStatus("PENDING");
        enrollmentRepository.save(enrollment);

        redirectAttributes.addFlashAttribute("success", "Enrollment request submitted successfully! Waiting for admin approval.");
        return "redirect:/courses/" + courseId;
    }
}
