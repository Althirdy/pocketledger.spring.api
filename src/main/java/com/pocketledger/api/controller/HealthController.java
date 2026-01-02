package com.pocketledger.api.controller;


import com.pocketledger.api.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ApiResponse<String> checkHealth(){
        return new ApiResponse<>("Api is Running",null, true);
    }
}
