package com.yumigo.dto.response.order;

import com.yumigo.enums.OrderStatus;
import com.yumigo.enums.PaymentMethod;
import com.yumigo.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id, String orderNumber, OrderStatus status,
        Long restaurantId, String restaurantName,
        List<OrderItemResponse> items,
        BigDecimal subtotal, BigDecimal deliveryFee, BigDecimal tax,
        BigDecimal discount, BigDecimal totalAmount,
        PaymentMethod paymentMethod, PaymentStatus paymentStatus,
        DeliveryAddressResponse deliveryAddress, String contactPhone,
        String specialInstructions, String cancelReason,
        LocalDateTime placedAt, LocalDateTime deliveredAt) {}
