package com.nureka.rest_api_mysql.service;

import com.nureka.rest_api_mysql.request.FinaliseResetPasswordRequest;
import com.nureka.rest_api_mysql.request.InitiateResetPasswordRequest;
import com.nureka.rest_api_mysql.request.LoginRequest;
import com.nureka.rest_api_mysql.request.RegisterRequest;
import com.nureka.rest_api_mysql.response.CommonResponse;

import java.util.HashMap;

public interface AuthService {
    CommonResponse<HashMap<String, Object>> registerUser(RegisterRequest registerRequest);

    CommonResponse<HashMap<String, Object>> loginUser(LoginRequest loginRequest);

    CommonResponse<HashMap<String, Object>> initiateResetPassword(InitiateResetPasswordRequest initiateResetPasswordRequest);

    CommonResponse<HashMap<String, Object>> finaliseResetPassword(FinaliseResetPasswordRequest finaliseResetPasswordRequest);
}
