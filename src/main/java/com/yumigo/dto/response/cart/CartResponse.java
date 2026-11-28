package com.yumigo.dto.response.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long restaurantId, String restaurantName,
        List<CartItemResponse> items, int itemCount, BigDecimal subtotal) {}
