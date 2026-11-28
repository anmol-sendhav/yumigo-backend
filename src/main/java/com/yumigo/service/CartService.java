package com.yumigo.service;

import com.yumigo.dto.request.cart.AddToCartRequest;
import com.yumigo.dto.request.cart.UpdateCartItemRequest;
import com.yumigo.dto.response.cart.CartResponse;

public interface CartService {
    CartResponse getCart(Long userId);
    CartResponse addItem(Long userId, AddToCartRequest request);
    CartResponse updateQuantity(Long userId, Long menuItemId, UpdateCartItemRequest request);
    CartResponse removeItem(Long userId, Long menuItemId);
    void clear(Long userId);
}
