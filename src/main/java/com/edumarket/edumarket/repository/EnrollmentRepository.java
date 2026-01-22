package com.edumarket.edumarket.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edumarket.edumarket.model.Enrollment;
import com.edumarket.edumarket.model.Course;
import com.edumarket.edumarket.model.User;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourse(Course course);
    List<Enrollment> findByUser(User user);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    List<Enrollment> findByCourseAndStatus(Course course, String status);
}
