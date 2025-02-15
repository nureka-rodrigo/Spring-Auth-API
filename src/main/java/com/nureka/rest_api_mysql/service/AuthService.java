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
import com.nureka.rest_api_mysql.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;
    @Value("${app.url}")
    private String appUrl;
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
    }

    public CommonResponse<HashMap<String, Object>> registerUser(RegisterRequest registerRequest) {
        try {
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                return new CommonResponse<>(Instant.now(), 400, "Email already exists", null);
            }

            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return new CommonResponse<>(Instant.now(), 400, "Passwords do not match", null);
            }

            User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(User.Role.USER);
            user.setCreatedAt(Instant.now());

            user = userRepository.save(user);

            HashMap<String, Object> data = new HashMap<>();
            data.put("firstName", user.getFirstName());
            data.put("lastName", user.getLastName());
            data.put("email", user.getEmail());
            data.put("role", user.getRole().name());

            return new CommonResponse<>(Instant.now(), 201, "User registered successfully", data);
        } catch (Exception e) {
            return new CommonResponse<>(Instant.now(), 500, e.getMessage(), null);
        }
    }

    public CommonResponse<HashMap<String, Object>> loginUser(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new CommonResponse<>(Instant.now(), 400, "Invalid email or password", null);
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new CommonResponse<>(Instant.now(), 400, "Invalid email or password", null);
            }

            String token = tokenProvider.generateToken(user.getEmail(), loginRequest.isRememberMe());

            HashMap<String, Object> data = new HashMap<>();
            data.put("firstName", user.getFirstName());
            data.put("lastName", user.getLastName());
            data.put("email", user.getEmail());
            data.put("role", user.getRole().name());
            data.put("token", token);

            return new CommonResponse<>(Instant.now(), 200, "Login successful", data);
        } catch (Exception e) {
            return new CommonResponse<>(Instant.now(), 500, e.getMessage(), null);
        }
    }

    @Transactional
    public CommonResponse<HashMap<String, Object>> initiateResetPassword(InitiateResetPasswordRequest initiateResetPasswordRequest) {
        try {
            User user = userRepository.findByEmail(initiateResetPasswordRequest.getEmail()).orElse(null);

            if (user == null) {
                return new CommonResponse<>(Instant.now(), 400, "Invalid email", null);
            }


            passwordResetTokenRepository.deleteByUserId(user);

            String token = tokenProvider.generateToken(user.getEmail(), false);
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUserId(user);
            passwordResetToken.setToken(token);
            passwordResetToken.setCreatedAt(Instant.now());
            passwordResetToken.setExpiredAt(Instant.now().plus(30, ChronoUnit.MINUTES));
            passwordResetTokenRepository.save(passwordResetToken);

            String resetLink = appUrl + ":" + serverPort + "/reset-password?token=" + token;
            emailService.sendEmail(user.getEmail(), "Password Reset Request", "To reset your password, click the link below:\n" + resetLink);

            return new CommonResponse<>(Instant.now(), 200, "A password reset link has been sent to your email", null);
        } catch (Exception e) {
            return new CommonResponse<>(Instant.now(), 500, e.getMessage(), null);
        }
    }

    public CommonResponse<HashMap<String, Object>> finaliseResetPassword(FinaliseResetPasswordRequest finaliseResetPasswordRequest) {
        try {
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(finaliseResetPasswordRequest.getToken());

            if (passwordResetToken.getExpiredAt().isBefore(Instant.now())) {
                return new CommonResponse<>(Instant.now(), 400, "Password reset token has expired", null);
            }

            if (!finaliseResetPasswordRequest.getPassword().equals(finaliseResetPasswordRequest.getConfirmPassword())) {
                return new CommonResponse<>(Instant.now(), 500, "Passwords do not match", null);
            }

            User user = passwordResetToken.getUserId();
            user.setPassword(passwordEncoder.encode(finaliseResetPasswordRequest.getPassword()));
            userRepository.save(user);

            passwordResetTokenRepository.delete(passwordResetToken);

            return new CommonResponse<>(Instant.now(), 200, "Password reset successful", null);
        } catch (Exception e) {
            return new CommonResponse<>(Instant.now(), 500, e.getMessage(), null);
        }
    }
}