package com.binaracademy.shoppingservice.client;

import com.binaracademy.shoppingservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", url = "${application.config.auth-url}")
public interface UserClient {
    @GetMapping("/auth/me")
    User getDetail();
}