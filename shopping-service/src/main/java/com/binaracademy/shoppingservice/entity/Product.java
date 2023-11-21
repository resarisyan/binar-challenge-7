package com.binaracademy.shoppingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_code")
    private UUID productCode;

    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;

    @Column(name = "price", nullable = false)
    private Double price;
}
