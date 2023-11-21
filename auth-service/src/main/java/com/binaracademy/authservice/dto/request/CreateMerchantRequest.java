package com.binaracademy.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMerchantRequest {
    private String merchantName;
    private String merchantLocation;
    private Boolean open;
    private String username;
}

