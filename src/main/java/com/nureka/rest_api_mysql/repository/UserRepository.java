package com.nureka.rest_api_mysql.repository;

import com.nureka.rest_api_mysql.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}