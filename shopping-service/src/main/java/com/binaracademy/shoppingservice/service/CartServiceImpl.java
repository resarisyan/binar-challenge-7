package com.binaracademy.shoppingservice.service;

import com.binaracademy.shoppingservice.client.ProductClient;
import com.binaracademy.shoppingservice.client.UserClient;
import com.binaracademy.shoppingservice.dto.request.CreateCartRequest;
import com.binaracademy.shoppingservice.dto.response.CartResponse;
import com.binaracademy.shoppingservice.dto.response.ProductResponse;
import com.binaracademy.shoppingservice.entity.Cart;
import com.binaracademy.shoppingservice.entity.Product;
import com.binaracademy.shoppingservice.entity.User;
import com.binaracademy.shoppingservice.exception.DataNotFoundException;
import com.binaracademy.shoppingservice.exception.ServiceBusinessException;
import com.binaracademy.shoppingservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    public CartResponse addNewCart(CreateCartRequest cart) {
        CartResponse cartResponse;
        try {
            User user = userClient.getDetail();
            Product product = productClient.getProduct(
                    cart.getProductName()).orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND)
            );
            Cart existingCart = cartRepository.findByProductAndUser(product, user);
            if (existingCart != null) {
                int newQuantity = existingCart.getQuantity() + cart.getQuantity();
                existingCart.setQuantity(newQuantity);
                double newTotalPrice = existingCart.getTotalPrice() + (product.getPrice() * cart.getQuantity());
                existingCart.setTotalPrice(newTotalPrice);
                cartRepository.save(existingCart);
            } else {
                Cart newCart = Cart.builder()
                        .quantity(cart.getQuantity())
                        .totalPrice(product.getPrice() * cart.getQuantity())
                        .product(product)
                        .username(user.getUsername())
                        .build();
                cartRepository.save(newCart);
            }
            cartResponse = CartResponse.builder()
                    .product(ProductResponse.builder()
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .build())
                    .quantity(cart.getQuantity())
                    .totalPrice(product.getPrice() * cart.getQuantity())
                    .build();
            log.info("Cart {} successfully added", cartResponse);
            return cartResponse;
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(PRODUCT_NOT_FOUND);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to add new cart");
        }
    }
}
