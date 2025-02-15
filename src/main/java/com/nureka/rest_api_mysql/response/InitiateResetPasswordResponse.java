package com.nureka.rest_api_mysql.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitiateResetPasswordResponse {
    private String message;
}