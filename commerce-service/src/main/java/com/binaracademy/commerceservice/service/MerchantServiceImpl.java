package com.binaracademy.commerceservice.service;

import com.binaracademy.commerceservice.client.AuthClient;
import com.binaracademy.commerceservice.dto.request.CreateMerchantRequest;
import com.binaracademy.commerceservice.dto.request.UpdateStatusMerchantRequest;
import com.binaracademy.commerceservice.dto.response.MerchantResponse;
import com.binaracademy.commerceservice.dto.response.UserResponse;
import com.binaracademy.commerceservice.entity.Merchant;
import com.binaracademy.commerceservice.entity.User;
import com.binaracademy.commerceservice.exception.DataNotFoundException;
import com.binaracademy.commerceservice.exception.ServiceBusinessException;
import com.binaracademy.commerceservice.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService{
    private final MerchantRepository merchantRepository;
    private final AuthClient userClient;
    @Override
    public void updateStatusMerchant(UpdateStatusMerchantRequest request) {
        try {
            log.info("Updating merchant");
            UserResponse user = userClient.getDetail();
            Merchant merchant = merchantRepository.findFirstByUsername(user.getUsername())
                    .orElseThrow(() -> new DataNotFoundException("Merchant not found"));
            merchant.setOpen(request.getOpen());
            merchantRepository.save(merchant);
            log.info("Merchant {} successfully updated", merchant.getMerchantName());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update user");
            throw new ServiceBusinessException("Failed to update user");
        }
    }

    @Override
    public Page<MerchantResponse> getAllMerchantByOpen(Boolean open, Pageable pageable) {
        try {
            log.info("Getting all merchant");
            Page<Merchant> merchantPage = Optional.of(merchantRepository.findByOpen(open, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException("No merchant found"));
            return  merchantPage.map(merchant -> MerchantResponse.builder()
                    .merchantName(merchant.getMerchantName())
                    .merchantLocation(merchant.getMerchantLocation())
                    .open(merchant.getOpen())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all merchant");
            throw new ServiceBusinessException("Failed to get all merchant");
        }
    }

    @Override
    public MerchantResponse createMerchant(CreateMerchantRequest request) {
        MerchantResponse merchantResponse;
        try {
            log.info("Creating merchant");
            Merchant merchant = Merchant.builder()
                    .merchantName(request.getMerchantName())
                    .merchantLocation(request.getMerchantLocation())
                    .username(request.getUsername())
                    .open(request.getOpen())
                    .build();
            merchantRepository.save(merchant);
            merchantResponse = MerchantResponse.builder()
                        .merchantName(merchant.getMerchantName())
                        .merchantLocation(merchant.getMerchantLocation())
                        .open(merchant.getOpen())
                        .build();
            log.info("Merchant {} successfully created", merchantResponse.getMerchantName());
            return merchantResponse;
        } catch (Exception e) {
            log.error("Failed to create merchant");
            throw new ServiceBusinessException("Failed to create merchant");
        }
    }
}
