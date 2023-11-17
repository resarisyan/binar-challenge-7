package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderService orderService;

    @Test
    void testMakeOrder() {
        OrderRequest request = new OrderRequest();
        OrderResponse orderResponse = new OrderResponse();

        when(orderService.makeOrder(request)).thenReturn(orderResponse);

        ResponseEntity<APIResultResponse<OrderResponse>> result = orderController.makeOrder(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Order successfully created", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(orderResponse, result.getBody().getResults());

        verify(orderService).makeOrder(request);
    }

    @Test
    void testGetAllOrder() {
        String username = "sampleUser";
        Page<OrderResponse> orderResponses = Page.empty();

        when(orderService.getAllOrderWithPagination(username, PageRequest.of(0, 5))).thenReturn(orderResponses);

        ResponseEntity<APIResultResponse<Page<OrderResponse>>> result = orderController.getAllOrder(0, username);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Merchant successfully retrieved", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(orderResponses, result.getBody().getResults());

        verify(orderService).getAllOrderWithPagination(username, PageRequest.of(0, 5));
    }
}
