package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.response.base.APIResultResponse;
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
class CartControllerTest {
    @InjectMocks
    private CartController cartController;
    @Mock
    private CartService cartService;

    @Test
    void testCreateNewUser() {
        CreateCartRequest createCartRequest = new CreateCartRequest("productA", 3);

        CartResponse cartResponse = new CartResponse();

        when(cartService.addNewCart(createCartRequest)).thenReturn(cartResponse);

        ResponseEntity<APIResultResponse<CartResponse>> result = cartController.addToCart(createCartRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Product successfully added to cart", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(cartResponse, result.getBody().getResults());

        verify(cartService).addNewCart(createCartRequest);
    }
}
