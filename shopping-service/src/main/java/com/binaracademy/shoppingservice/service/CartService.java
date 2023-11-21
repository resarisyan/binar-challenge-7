package com.binaracademy.shoppingservice.service;

import com.binaracademy.shoppingservice.dto.request.CreateCartRequest;
import com.binaracademy.shoppingservice.dto.response.CartResponse;

public interface CartService {
    CartResponse addNewCart(CreateCartRequest cart);
}
