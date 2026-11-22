package com.yumigo.dto.request.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(

        @NotNull(message = "Enter a valid category id")
        Long categoryId,

        @NotBlank(message = "Enter a valid name")
        @Size(max=150,message = "Name is required")
        String name,

        @Size(min=500)
        String description,

        @Positive
        BigDecimal price,

        boolean veg,

        boolean available,

        @Size(max=500)
        String imageUrl,

        @Positive
        Integer preparationTimeMins
) {
}
