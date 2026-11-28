package com.yumigo.dto.response.order;

import com.yumigo.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long id, String orderNumber, OrderStatus status,
        String restaurantName, BigDecimal totalAmount, LocalDateTime placedAt) {}
