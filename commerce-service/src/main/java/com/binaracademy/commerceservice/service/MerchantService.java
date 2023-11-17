package com.binaracademy.commerceservice.service;

import com.binaracademy.commerceservice.dto.request.UpdateStatusMerchantRequest;
import com.binaracademy.commerceservice.dto.response.MerchantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MerchantService {
    void updateStatusMerchant(UpdateStatusMerchantRequest request);
    Page<MerchantResponse> getAllMerchantByOpen(Boolean open, Pageable pageable);
}
