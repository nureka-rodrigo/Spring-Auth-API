package com.nureka.rest_api_mysql.service;

import com.nureka.rest_api_mysql.model.User;
import com.nureka.rest_api_mysql.repository.UserRepository;
import com.nureka.rest_api_mysql.request.LoginRequest;
import com.nureka.rest_api_mysql.request.RegisterRequest;
import com.nureka.rest_api_mysql.response.LoginResponse;
import com.nureka.rest_api_mysql.response.RegisterResponse;
import com.nureka.rest_api_mysql.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Check if the email is already in use
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Check if the password and confirm password match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Create a new user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.USER);
        user.setCreatedAt(Instant.now());

        return new RegisterResponse(userRepository.save(user).getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name());
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        // Check if the user exists
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check if the password is correct
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate a JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name(), token);
    }
}