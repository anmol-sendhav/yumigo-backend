package com.yumigo.dto.request.order;

import com.yumigo.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateOrderStatusRequest(
        @NotNull(message = "Status is required") OrderStatus status,
        @Size(max = 255, message = "Remark must be at most 255 characters") String remark
) {}
