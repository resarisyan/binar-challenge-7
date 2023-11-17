package com.binaracademy.commerceservice.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchant")
public class Merchant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "merchant_code")
    private UUID merchantCode;

    @Column(name = "merchant_name", nullable = false, unique = true)
    private String merchantName;

    @Column(name = "merchant_location", nullable = false)
    private String merchantLocation;

    @Column(name = "open", nullable = false)
    private Boolean open;

    private String username;
}
