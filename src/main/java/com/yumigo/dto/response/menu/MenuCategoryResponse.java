package com.yumigo.dto.response.menu;

public record MenuCategoryResponse(
        Long id,
        String name,
        Integer displayOrder
) {
}
