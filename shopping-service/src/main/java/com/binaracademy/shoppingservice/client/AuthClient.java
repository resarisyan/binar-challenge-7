package com.binaracademy.shoppingservice.client;

import com.binaracademy.shoppingservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", url = "${application.config.auth-url}v1/auth")
public interface AuthClient {
    @GetMapping("/me")
    User getDetail();
}