package com.yumigo.dto.request.restaurant;

import jakarta.validation.constraints.NotNull;

public record RestaurantOpenRequest(
        @NotNull(message = "Open status is required") Boolean open
) {}
