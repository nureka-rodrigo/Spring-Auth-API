package com.nureka.rest_api_mysql.controller;

import com.nureka.rest_api_mysql.request.FinaliseResetPasswordRequest;
import com.nureka.rest_api_mysql.request.InitiateResetPasswordRequest;
import com.nureka.rest_api_mysql.request.LoginRequest;
import com.nureka.rest_api_mysql.request.RegisterRequest;
import com.nureka.rest_api_mysql.response.FinaliseResetPasswordResponse;
import com.nureka.rest_api_mysql.response.InitiateResetPasswordResponse;
import com.nureka.rest_api_mysql.response.LoginResponse;
import com.nureka.rest_api_mysql.response.RegisterResponse;
import com.nureka.rest_api_mysql.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Validated @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.registerUser(registerRequest);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginUser(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<InitiateResetPasswordResponse> initiateResetPassword(@Validated @RequestBody InitiateResetPasswordRequest initiateResetPasswordRequest) {
        InitiateResetPasswordResponse initiateResetPasswordResponse = authService.initiateResetPassword(initiateResetPasswordRequest);
        return new ResponseEntity<>(initiateResetPasswordResponse, HttpStatus.OK);
    }

    @PostMapping("/finalise-reset-password")
    public ResponseEntity<FinaliseResetPasswordResponse> finaliseResetPassword(@Validated @RequestBody FinaliseResetPasswordRequest finaliseResetPasswordRequest) {
        FinaliseResetPasswordResponse finaliseResetPasswordResponse = authService.finaliseResetPassword(finaliseResetPasswordRequest);
        return new ResponseEntity<>(finaliseResetPasswordResponse, HttpStatus.OK);
    }
}