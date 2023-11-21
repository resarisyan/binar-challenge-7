package com.binaracademy.shoppingservice.client;

import com.binaracademy.shoppingservice.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@FeignClient(name = "commerce-service", url = "${application.config.commerce-url}")
public interface ProductClient {
    @GetMapping("/{productName}")
    Optional<Product> getProduct(String productName);
}
