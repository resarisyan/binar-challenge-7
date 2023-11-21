package com.binaracademy.commerceservice.client;

import com.binaracademy.commerceservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "commerce-service", url = "${application.config.auth-url}v1/auth")
public interface AuthClient {
    @GetMapping("/me")
    UserResponse getDetail();
}