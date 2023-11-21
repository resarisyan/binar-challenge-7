package com.binaracademy.shoppingservice.repository;

import com.binaracademy.shoppingservice.entity.Cart;
import com.binaracademy.shoppingservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findByUsernameAndProduct(String username, Product product);

    List<Cart> findByUsername(String username);
}
