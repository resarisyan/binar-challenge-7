package com.binaracademy.commerceservice.repository;

import com.binaracademy.commerceservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{
    Optional<Product> findByProductName(String productName);
    Page<Product> findAll(Pageable pageable);
    @Query(value = """
        SELECT p FROM Product p
        WHERE p.productName = :productName AND p.productName <> :excludeProductName
        """)
    Optional<Product> findProductWithExcludedValue(String productName, String excludeProductName);
}
