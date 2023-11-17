package com.binaracademy.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {
    private String merchantName;
    private String merchantLocation;
    private Boolean open;
    private UUID userId;
}
