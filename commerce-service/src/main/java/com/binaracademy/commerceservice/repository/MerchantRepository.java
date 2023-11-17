package com.binaracademy.commerceservice.repository;

import com.binaracademy.commerceservice.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    Optional<Merchant> findFirstByUsername(String username);
//    Optional<Merchant> findFirstByUser(User user);
    Page<Merchant> findByOpen(Boolean open, Pageable pageable);
}
