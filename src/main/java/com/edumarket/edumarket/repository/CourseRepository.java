package com.edumarket.edumarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edumarket.edumarket.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
