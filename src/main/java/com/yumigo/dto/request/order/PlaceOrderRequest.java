package com.yumigo.dto.request.order;

import com.yumigo.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlaceOrderRequest(
        @NotNull(message = "Delivery address is required") Long addressId,
        @NotNull(message = "Payment method is required") PaymentMethod paymentMethod,
        @Size(max = 500, message = "Instructions must be at most 500 characters") String specialInstructions
) {}
