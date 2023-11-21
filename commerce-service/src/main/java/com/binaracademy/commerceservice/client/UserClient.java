package com.binaracademy.commerceservice.client;

import com.binaracademy.commerceservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "commerce-service", url = "${application.config.auth-url}")
public interface UserClient {
    @GetMapping("/auth/me")
    User getDetail();
}