package com.yumigo.dto.request.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuItemAvailabilityRequest(

        @NotNull(message = "Availability is required")
        boolean available
) {
}
