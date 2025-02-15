package com.nureka.rest_api_mysql.controller;

import com.nureka.rest_api_mysql.response.CommonResponse;
import com.nureka.rest_api_mysql.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<CommonResponse<HashMap<String, Object>>> dashboard() {
        CommonResponse<HashMap<String, Object>> response = adminService.dashboard();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
