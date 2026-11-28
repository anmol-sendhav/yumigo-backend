package com.yumigo.dto.response.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long menuItemId, String name, BigDecimal unitPrice,
        int quantity, BigDecimal lineTotal, boolean available) {}
