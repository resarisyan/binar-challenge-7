package com.binaracademy.shoppingservice.controller;

import com.binaracademy.shoppingservice.dto.request.OrderRequest;
import com.binaracademy.shoppingservice.dto.response.OrderResponse;
import com.binaracademy.shoppingservice.dto.response.base.APIResultResponse;
import com.binaracademy.shoppingservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/order", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order API")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/")
    @Schema(name = "OrderRequest", description = "Order request body")
    @Operation(summary = "Endpoint to handle order")
    public ResponseEntity<APIResultResponse<OrderResponse>>  makeOrder(@RequestBody @Valid OrderRequest request) {
        OrderResponse orderResponse = orderService.makeOrder(request);
        APIResultResponse<OrderResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Order successfully created",
                orderResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @GetMapping("/")
    @Schema(name = "GetAllOrderRequest", description = "Get all order request body")
    @Operation(summary = "Endpoint to handle get all order")
    public ResponseEntity<APIResultResponse<Page<OrderResponse>>> getAllOrder(
            @RequestParam("page") int page, @RequestParam("username") String username) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<OrderResponse> orderResponses = orderService.getAllOrderWithPagination(username, pageable);
        APIResultResponse<Page<OrderResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Merchant successfully retrieved",
                orderResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
