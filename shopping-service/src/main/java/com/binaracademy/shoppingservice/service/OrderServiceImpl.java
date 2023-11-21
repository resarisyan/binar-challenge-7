package com.binaracademy.shoppingservice.service;

import com.binaracademy.shoppingservice.client.AuthClient;
import com.binaracademy.shoppingservice.dto.request.OrderRequest;
import com.binaracademy.shoppingservice.dto.response.*;
import com.binaracademy.shoppingservice.entity.*;
import com.binaracademy.shoppingservice.exception.DataNotFoundException;
import com.binaracademy.shoppingservice.exception.ServiceBusinessException;
import com.binaracademy.shoppingservice.repository.CartRepository;
import com.binaracademy.shoppingservice.repository.OrderDetailRepository;
import com.binaracademy.shoppingservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AuthClient userClient;

    @Override
    public OrderResponse makeOrder(OrderRequest request) {
        try {
            User user = userClient.getDetail();
            List<Cart> carts = cartRepository.findByUsername(user.getUsername());
            if (carts.isEmpty()) {
                throw new DataNotFoundException("Cart is empty");
            }
            Order order = Order.builder()
                    .username(user.getUsername())
                    .completed(false)
                    .destinationAddress(request.getDestinationAddress())
                    .note(request.getNote())
                    .orderTime(new Date())
                    .build();
            Order newOrder = orderRepository.save(order);
            List<OrderDetail> orderDetails = new ArrayList<>();
            carts.forEach(cart -> {
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(newOrder)
                        .product(cart.getProduct())
                        .quantity(cart.getQuantity())
                        .totalPrice(cart.getTotalPrice())
                        .build();
                OrderDetail newOrderDetail = orderDetailRepository.save(orderDetail);
                orderDetails.add(newOrderDetail);
            });
            order.setOrderDetails(orderDetails);
            return OrderResponse.builder()
                    .orderTime(order.getOrderTime())
                    .destinationAddress(order.getDestinationAddress())
                    .note(order.getNote())
                    .completed(order.getCompleted())
                    .orderDetails(
                            order.getOrderDetails().stream().map(orderDetail -> OrderDetailResponse.builder()
                                    .product(
                                            ProductResponse.builder()
                                                    .productName(orderDetail.getProduct().getProductName())
                                                    .price(orderDetail.getProduct().getPrice())
                                                    .build()
                                    )
                                    .quantity(orderDetail.getQuantity())
                                    .totalPrice(orderDetail.getTotalPrice())
                                    .build()).toList()
                    )
                    .build();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to make order");
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public Page<OrderResponse> getAllOrderWithPagination(String username, Pageable pageable) {
        try {
            log.info("Getting all order");
            User user = userClient.getDetail();
            Page<Order> orderPage = Optional.of(orderRepository.findAllByUsername(user.getUsername(), pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException("No order found"));
            return orderPage.map(order -> OrderResponse.builder()
                    .orderTime(order.getOrderTime())
                    .destinationAddress(order.getDestinationAddress())
                    .note(order.getNote())
                    .completed(order.getCompleted())
                    .orderDetails(
                            order.getOrderDetails().stream().map(orderDetail -> OrderDetailResponse.builder()
                                    .product(
                                            ProductResponse.builder()
                                                    .productName(orderDetail.getProduct().getProductName())
                                                    .price(orderDetail.getProduct().getPrice())
                                                    .build()
                                    )
                                    .quantity(orderDetail.getQuantity())
                                    .totalPrice(orderDetail.getTotalPrice())
                                    .build()).toList()
                    )
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all order");
            throw new ServiceBusinessException("Failed to get all order");
        }
    }

    @Override
    public void deleteOrdersOlderThanThreeMonths() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        Date threeMonthsAgoDate = java.sql.Timestamp.valueOf(threeMonthsAgo);

        orderRepository.deleteByOrderTimeBefore(threeMonthsAgoDate);
    }

}
