package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.config.JwtService;
import com.binaracademy.authservice.dto.request.LoginRequest;
import com.binaracademy.authservice.dto.request.RegisterCustomerRequest;
import com.binaracademy.authservice.dto.request.RegisterMerchantRequest;
import com.binaracademy.authservice.dto.response.*;
import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import com.binaracademy.authservice.entity.User;
import com.binaracademy.authservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/v1/auth", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/register/customer")
    public ResponseEntity<APIResultResponse<RegisterCustomerResponse>> registerCustomer(@RequestBody @Valid RegisterCustomerRequest request) {
        RegisterCustomerResponse customerResponse = authenticationService.registerCustomer(request);
        APIResultResponse<RegisterCustomerResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.CREATED,
                "Customer successfully created",
                customerResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/register/merchant")
    public ResponseEntity<CompletableFuture<APIResultResponse<RegisterMerchantResponse>>> registerMerchantAsync(@RequestBody @Valid RegisterMerchantRequest request) {
        CompletableFuture<APIResultResponse<RegisterMerchantResponse>> responseFuture =
                authenticationService.registerMerchantAsync(request)
                        .thenApply(merchantResponse -> new APIResultResponse<>(
                                HttpStatus.CREATED,
                                "Merchant successfully created",
                                merchantResponse));
        return new ResponseEntity<>(responseFuture, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    @Hidden
    public ResponseEntity<APIResultResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authenticationService.login(request);
        APIResultResponse<LoginResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.CREATED,
                "Login success",
                loginResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<APIResultResponse<RefreshTokenResponse>> refreshToken(HttpServletRequest request, HttpServletResponse response)  {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request, response);
        APIResultResponse<RefreshTokenResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.CREATED,
                "Refresh token success",
                refreshTokenResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getDetail() {
        User user = jwtService.getUser();
        UserResponse userResponse = UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
