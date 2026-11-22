package com.yumigo.dto.response.menu;

import java.util.List;

public record MenuCategoryWithItemsResponse(
        Long id,
        String name,
        Integer displayOrder,
        List<MenuItemResponse> items
) {
}
