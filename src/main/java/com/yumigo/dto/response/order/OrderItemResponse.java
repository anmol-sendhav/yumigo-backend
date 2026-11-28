package com.yumigo.dto.response.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        String itemName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal) {}

