package com.nureka.rest_api_mysql.service;

import com.nureka.rest_api_mysql.response.CommonResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public CommonResponse<HashMap<String, Object>> dashboard() {
        return new CommonResponse<>(Instant.now(), 200, "User dashboard", null);
    }
}
