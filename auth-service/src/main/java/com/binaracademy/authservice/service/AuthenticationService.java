package com.binaracademy.authservice.service;

import com.binaracademy.authservice.dto.request.LoginRequest;
import com.binaracademy.authservice.dto.request.RegisterCustomerRequest;
import com.binaracademy.authservice.dto.request.RegisterMerchantRequest;
import com.binaracademy.authservice.dto.response.LoginResponse;
import com.binaracademy.authservice.dto.response.RefreshTokenResponse;
import com.binaracademy.authservice.dto.response.RegisterCustomerResponse;
import com.binaracademy.authservice.dto.response.RegisterMerchantResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.CompletableFuture;

public interface AuthenticationService {
    RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request);
    CompletableFuture<RegisterMerchantResponse> registerMerchantAsync(RegisterMerchantRequest request);

    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
