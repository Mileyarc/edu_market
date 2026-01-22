package com.edumarket.edumarket.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edumarket.edumarket.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
} 