package com.nureka.rest_api_mysql.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String token;
}