package com.nureka.rest_api_mysql.controller;

import com.nureka.rest_api_mysql.request.FinaliseResetPasswordRequest;
import com.nureka.rest_api_mysql.request.InitiateResetPasswordRequest;
import com.nureka.rest_api_mysql.request.LoginRequest;
import com.nureka.rest_api_mysql.request.RegisterRequest;
import com.nureka.rest_api_mysql.response.CommonResponse;
import com.nureka.rest_api_mysql.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<HashMap<String, Object>>> registerUser(@Validated @RequestBody RegisterRequest registerRequest) {
        CommonResponse<HashMap<String, Object>> registerResponse = authService.registerUser(registerRequest);
        return new ResponseEntity<>(registerResponse, HttpStatus.valueOf(registerResponse.getStatus()));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<HashMap<String, Object>>> login(@Validated @RequestBody LoginRequest loginRequest) {
        CommonResponse<HashMap<String, Object>> loginResponse = authService.loginUser(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.valueOf(loginResponse.getStatus()));
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<CommonResponse<HashMap<String, Object>>> initiateResetPassword(@Validated @RequestBody InitiateResetPasswordRequest initiateResetPasswordRequest) {
        CommonResponse<HashMap<String, Object>> initiateResetPasswordResponse = authService.initiateResetPassword(initiateResetPasswordRequest);
        return new ResponseEntity<>(initiateResetPasswordResponse, HttpStatus.valueOf(initiateResetPasswordResponse.getStatus()));
    }

    @PostMapping("/finalise-reset-password")
    public ResponseEntity<CommonResponse<HashMap<String, Object>>> finaliseResetPassword(@Validated @RequestBody FinaliseResetPasswordRequest finaliseResetPasswordRequest) {
        CommonResponse<HashMap<String, Object>> finaliseResetPasswordResponse = authService.finaliseResetPassword(finaliseResetPasswordRequest);
        return new ResponseEntity<>(finaliseResetPasswordResponse, HttpStatus.valueOf(finaliseResetPasswordResponse.getStatus()));
    }
}