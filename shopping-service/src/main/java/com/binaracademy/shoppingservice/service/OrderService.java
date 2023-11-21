package com.binaracademy.shoppingservice.service;

import com.binaracademy.shoppingservice.dto.request.OrderRequest;
import com.binaracademy.shoppingservice.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse makeOrder(OrderRequest request);

    Page<OrderResponse> getAllOrderWithPagination(String username, Pageable pageable);

    void deleteOrdersOlderThanThreeMonths();
}
