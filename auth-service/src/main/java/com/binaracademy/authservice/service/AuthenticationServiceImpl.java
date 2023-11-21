package com.binaracademy.authservice.service;

import com.binaracademy.authservice.client.MerchantClient;
import com.binaracademy.authservice.config.JwtService;
import com.binaracademy.authservice.dto.request.CreateMerchantRequest;
import com.binaracademy.authservice.dto.request.LoginRequest;
import com.binaracademy.authservice.dto.request.RegisterCustomerRequest;
import com.binaracademy.authservice.dto.request.RegisterMerchantRequest;
import com.binaracademy.authservice.dto.response.*;
import com.binaracademy.authservice.entity.Token;
import com.binaracademy.authservice.entity.User;
import com.binaracademy.authservice.enumeration.EnumRole;
import com.binaracademy.authservice.enumeration.EnumTokenType;
import com.binaracademy.authservice.exception.DataConflictException;
import com.binaracademy.authservice.exception.DataNotFoundException;
import com.binaracademy.authservice.exception.ServiceBusinessException;
import com.binaracademy.authservice.repository.TokenRepository;
import com.binaracademy.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MerchantClient merchantClient;
    public LoginResponse login(LoginRequest request) {
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getUsername(),
                           request.getPassword()
                   )
           );
           User user = userRepository.findFirstByUsername(request.getUsername())
                   .orElseThrow(() -> new DataNotFoundException("User not found"));
           String jwtToken = jwtService.generateToken(user);
           String refreshToken = jwtService.generateRefreshToken(user);
           revokeAllUserTokens(user);
           saveUserToken(user, jwtToken);
           return LoginResponse.builder()
                   .accessToken(jwtToken)
                   .refreshToken(refreshToken)
                   .build();
       } catch (DataNotFoundException e) {
              throw e;
       } catch (Exception e) {
           throw new ServiceBusinessException("Failed to login");
       }
    }
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws ServiceBusinessException {
        RegisterCustomerResponse customerResponse;
        try{
            log.info("Adding new customer");
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(EnumRole.CUSTOMER)
                    .build();
            userRepository.save(user);
            customerResponse = RegisterCustomerResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        } catch (Exception e) {
            log.error("Failed to add new customer");
            throw new ServiceBusinessException("Failed to add new customer");
        }

        log.info("Customer {} successfully added", customerResponse.getUsername());
        return customerResponse;
    }

    @Async
    @Transactional
    public CompletableFuture<RegisterMerchantResponse> registerMerchantAsync(RegisterMerchantRequest request) {
        try {
            return CompletableFuture.completedFuture(registerMerchant(request));
        } catch (ServiceBusinessException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public RegisterMerchantResponse registerMerchant(RegisterMerchantRequest request) throws ServiceBusinessException {
        RegisterMerchantResponse registerMerchantResponse;
        MerchantResponse merchantResponse;
        try {
            log.info("Adding new merchant");
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(EnumRole.MERCHANT)
                    .build();
            user = userRepository.save(user);

            CreateMerchantRequest createMerchantRequest = CreateMerchantRequest.builder()
                    .merchantName(request.getMerchantName())
                    .merchantLocation(request.getMerchantLocation())
                    .open(request.getOpen())
                    .username(user.getUsername())
                    .build();

            CompletableFuture<MerchantResponse> merchantResponseFuture = CompletableFuture.supplyAsync(() ->
                    merchantClient.createMerchant(createMerchantRequest));

            merchantResponse = merchantResponseFuture.join(); // Wait for the merchant response

            if (merchantResponse == null) {
                throw new ServiceBusinessException("Failed to add new merchant");
            }

            registerMerchantResponse = RegisterMerchantResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .merchantName(merchantResponse.getMerchantName())
                    .merchantLocation(merchantResponse.getMerchantLocation())
                    .open(merchantResponse.getOpen())
                    .build();
        } catch (DataConflictException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to add new merchant");
            throw new ServiceBusinessException("Failed to add new merchant");
        }

        log.info("Merchant {} successfully added", registerMerchantResponse.getUsername());
        return registerMerchantResponse;
    }

    public RefreshTokenResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        RefreshTokenResponse refreshTokenResponse;

        try {
           final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
           final String refreshToken;
           final String username;
           if (authHeader == null || !authHeader.startsWith("Bearer ")) {
               throw new DataNotFoundException("Refresh token is missing");
           }
           refreshToken = authHeader.substring(7);
           username = jwtService.extractUsername(refreshToken);
           if (username == null) {
               throw new ServiceBusinessException("Refresh token is invalid");
           }

           var user = this.userRepository.findFirstByUsername(username)
                       .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                   var accessToken = jwtService.generateToken(user);
                   revokeAllUserTokens(user);
                   saveUserToken(user, accessToken);
                   refreshTokenResponse =  RefreshTokenResponse.builder()
                           .accessToken(accessToken)
                           .build();
            } else {
                   throw new ServiceBusinessException("Refresh token is invalid");
            }
        } catch (DataNotFoundException | ServiceBusinessException e) {
            throw e;
        } catch (Exception e) {
              throw new ServiceBusinessException("Failed to refresh token");
        }
         return refreshTokenResponse;
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(EnumTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public MerchantClient getMerchantClient() {
        return merchantClient;
    }
}
