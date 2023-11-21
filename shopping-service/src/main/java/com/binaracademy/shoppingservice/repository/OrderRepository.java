package com.binaracademy.shoppingservice.repository;

import com.binaracademy.shoppingservice.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByUsername(String username, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.orderTime < :thresholdDate")
    void deleteByOrderTimeBefore(Date thresholdDate);
}
