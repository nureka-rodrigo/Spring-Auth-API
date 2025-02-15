package com.nureka.rest_api_mysql.service;

import com.nureka.rest_api_mysql.response.CommonResponse;

import java.util.HashMap;

public interface UserService {
    CommonResponse<HashMap<String, Object>> dashboard();
}
