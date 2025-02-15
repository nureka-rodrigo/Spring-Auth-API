package com.nureka.rest_api_mysql.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FinaliseResetPasswordRequest {
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @NotBlank
    @Size(min = 8, max = 255)
    private String confirmPassword;

    @NotBlank
    @Size(max = 255)
    private String token;
}
