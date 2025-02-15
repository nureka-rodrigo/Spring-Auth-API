package com.nureka.rest_api_mysql.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InitiateResetPasswordRequest {
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
}
