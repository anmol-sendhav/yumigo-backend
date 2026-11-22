package com.yumigo.dto.request.restaurant;

import com.yumigo.enums.RestaurantStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantStatusUpdateRequest(

        @NotNull(message = "Status is required")
        RestaurantStatus status,

        @Size(max = 255, message = "Reason cannot exceed 255 characters")
        String reason
) {
}
