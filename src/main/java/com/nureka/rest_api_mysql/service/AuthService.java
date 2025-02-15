package com.nureka.rest_api_mysql.service;

import com.nureka.rest_api_mysql.config.TokenProvider;
import com.nureka.rest_api_mysql.model.PasswordResetToken;
import com.nureka.rest_api_mysql.model.User;
import com.nureka.rest_api_mysql.repository.PasswordResetTokenRepository;
import com.nureka.rest_api_mysql.repository.UserRepository;
import com.nureka.rest_api_mysql.request.FinaliseResetPasswordRequest;
import com.nureka.rest_api_mysql.request.InitiateResetPasswordRequest;
import com.nureka.rest_api_mysql.request.LoginRequest;
import com.nureka.rest_api_mysql.request.RegisterRequest;
import com.nureka.rest_api_mysql.response.FinaliseResetPasswordResponse;
import com.nureka.rest_api_mysql.response.InitiateResetPasswordResponse;
import com.nureka.rest_api_mysql.response.LoginResponse;
import com.nureka.rest_api_mysql.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
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
        String token = tokenProvider.generateToken(user.getEmail());

        return new LoginResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name(), token);
    }

    @Transactional
    public InitiateResetPasswordResponse initiateResetPassword(InitiateResetPasswordRequest initiateResetPasswordRequest) {
        // Check if the user exists
        User user = userRepository.findByEmail(initiateResetPasswordRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        // Delete any previous tokens requested by the user
        passwordResetTokenRepository.deleteByUserId(user);

        // Create a new token
        String token = tokenProvider.generateToken(user.getEmail());
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUserId(user);
        passwordResetToken.setToken(token);
        passwordResetToken.setCreatedAt(Instant.now());
        passwordResetToken.setExpiredAt(Instant.now().plus(30, ChronoUnit.MINUTES));
        passwordResetTokenRepository.save(passwordResetToken);

        // Generate a reset link
        String resetLink = "https://localhost:8080/reset-password?token=" + token;

        // Send email
        emailService.sendEmail(user.getEmail(), "Password Reset Request", "To reset your password, click the link below:\n" + resetLink);

        return new InitiateResetPasswordResponse("A password reset link has been sent to your email");
    }

    public FinaliseResetPasswordResponse finaliseResetPassword(FinaliseResetPasswordRequest finaliseResetPasswordRequest) {
        // Check if the token exists
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(finaliseResetPasswordRequest.getToken());

        // Check if the token has expired
        if (passwordResetToken.getExpiredAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        // Check if the password and confirm password match
        if (!finaliseResetPasswordRequest.getPassword().equals(finaliseResetPasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Update the user's password
        User user = passwordResetToken.getUserId();
        user.setPassword(passwordEncoder.encode(finaliseResetPasswordRequest.getPassword()));
        userRepository.save(user);

        // Delete the token
        passwordResetTokenRepository.delete(passwordResetToken);

        return new FinaliseResetPasswordResponse("Password reset successful");
    }
}