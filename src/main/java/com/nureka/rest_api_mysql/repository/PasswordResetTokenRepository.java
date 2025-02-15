package com.nureka.rest_api_mysql.repository;

import com.nureka.rest_api_mysql.model.PasswordResetToken;
import com.nureka.rest_api_mysql.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    void deleteByUserId(User user);
}