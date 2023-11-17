package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.request.LoginRequest;
import com.binaracademy.authservice.dto.request.RegisterCustomerRequest;
import com.binaracademy.authservice.dto.request.RegisterMerchantRequest;
import com.binaracademy.authservice.dto.response.LoginResponse;
import com.binaracademy.authservice.dto.response.RegisterCustomerResponse;
import com.binaracademy.authservice.dto.response.RegisterMerchantResponse;
import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import com.binaracademy.authservice.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;
    @Mock
    private AuthenticationService authenticationService;

    @Test
    void testRegisterCustomer() {
        RegisterCustomerRequest request = new RegisterCustomerRequest();
        request.setUsername("testUser");
        request.setEmail("test@example.com");
        request.setPassword("testPassword");

        RegisterCustomerResponse response = new RegisterCustomerResponse();
        response.setUsername(request.getUsername());
        response.setEmail(request.getEmail());

        when(authenticationService.registerCustomer(request)).thenReturn(response);

        ResponseEntity<APIResultResponse<RegisterCustomerResponse>> result = authenticationController.registerCustomer(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Customer successfully created", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(authenticationService).registerCustomer(request);
    }

    @Test
    void testRegisterMerchant() {
        RegisterMerchantRequest request = new RegisterMerchantRequest();
        request.setUsername("testUser");
        request.setEmail("test@example.com");
        request.setPassword("testPassword");
        request.setMerchantName("testMerchant");
        request.setMerchantLocation("testLocation");
        request.setOpen(true);

        RegisterMerchantResponse response = new RegisterMerchantResponse();
        response.setUsername(request.getUsername());
        response.setEmail(request.getEmail());
        response.setMerchantName(request.getMerchantName());
        response.setMerchantLocation(request.getMerchantLocation());
        response.setOpen(request.getOpen());

        when(authenticationService.registerMerchant(request)).thenReturn(response);

        ResponseEntity<APIResultResponse<RegisterMerchantResponse>> result = authenticationController.registerMerchant(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Merchant successfully created", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(authenticationService).registerMerchant(request);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        LoginResponse response = new LoginResponse();
        response.setAccessToken("testAccessToken");
        response.setRefreshToken("testRefreshToken");

        when(authenticationService.login(request)).thenReturn(response);
        ResponseEntity<APIResultResponse<LoginResponse>> result = authenticationController.login(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Login success", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(authenticationService).login(request);
    }
}
