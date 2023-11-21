package com.binaracademy.shoppingservice.repository;

import com.binaracademy.shoppingservice.entity.Cart;
import com.binaracademy.shoppingservice.entity.Product;
import com.binaracademy.shoppingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findByProductAndUser(Product product, User user);
    List<Cart> findByUser(User user);
}
