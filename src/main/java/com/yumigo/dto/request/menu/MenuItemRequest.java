package com.yumigo.dto.request.menu;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MenuItemRequest(

        @NotNull(message = "Category is required")
        Long categoryId,

        @NotBlank(message = "Item name is required")
        @Size(max = 150, message = "Item name must be at most 150 characters")
        String name,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "Price allows up to 8 digits and 2 decimals")
        BigDecimal price,

        @NotNull(message = "Specify whether the item is vegetarian")
        Boolean veg,

        Boolean available,                       // optional: the service treats null as true

        @Size(max = 500, message = "Image URL must be at most 500 characters")
        String imageUrl,

        @Positive(message = "Preparation time must be positive")
        Integer preparationTimeMins
) {}
