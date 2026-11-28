package com.yumigo.dto.request.order;

import jakarta.validation.constraints.Size;

public record CancelOrderRequest(
        @Size(max = 255, message = "Reason must be at most 255 characters") String reason
) {}
