package com.nureka.rest_api_mysql.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
@Data
public class CommonResponse<T> {
    private Instant timestamp;
    private int status;
    private String message;
    private T data;
}