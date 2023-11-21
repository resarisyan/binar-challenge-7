package com.binaracademy.authservice.client;

import com.binaracademy.authservice.dto.request.CreateMerchantRequest;
import com.binaracademy.authservice.dto.response.MerchantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(name = "commerce-service", url = "${application.config.commerce-url}/merchant")
public interface MerchantClient {
    @PostMapping("/")
    MerchantResponse createMerchant(CreateMerchantRequest request);
}