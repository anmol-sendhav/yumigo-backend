package com.yumigo.dto.response.menu;

import com.yumigo.dto.response.restaurant.RestaurantSummaryResponse;

import java.util.List;

public record RestaurantMenuResponse(
        RestaurantSummaryResponse restaurant,
        List<MenuCategoryWithItemsResponse> categories
) {}
