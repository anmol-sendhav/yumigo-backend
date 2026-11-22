package com.yumigo.dto.response.menu;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        boolean veg,
        boolean available,
        String imageUrl,
        Integer preparationTimeMins
) {
}
