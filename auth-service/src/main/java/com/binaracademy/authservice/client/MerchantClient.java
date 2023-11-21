package com.binaracademy.authservice.client;

import com.binaracademy.authservice.entity.Merchant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "commerce-service", url = "${application.config.commerce-url}/merchant")
public interface MerchantClient {
    @GetMapping("/merchant-id}")
    List<Merchant> findAllStudentsBySchool(@PathVariable("merchant-id") Integer merchantId);
    @PostMapping("/")
    Merchant createMerchant(Merchant merchant);
}