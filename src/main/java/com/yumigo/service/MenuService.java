package com.yumigo.service;

import com.yumigo.dto.request.menu.MenuCategoryRequest;
import com.yumigo.dto.request.menu.MenuItemAvailabilityRequest;
import com.yumigo.dto.request.menu.MenuItemRequest;
import com.yumigo.dto.response.menu.MenuCategoryResponse;
import com.yumigo.dto.response.menu.MenuItemResponse;
import com.yumigo.dto.response.menu.RestaurantMenuResponse;

public interface MenuService {
    MenuCategoryResponse createCategory(Long restaurantId, MenuCategoryRequest request, Long userId);
    MenuCategoryResponse updateCategory(Long restaurantId, Long categoryId, MenuCategoryRequest request, Long userId);
    void deleteCategory(Long restaurantId, Long categoryId, Long userId);

    MenuItemResponse createItem(Long restaurantId, MenuItemRequest request, Long userId);
    MenuItemResponse updateItem(Long restaurantId, Long itemId, MenuItemRequest request, Long userId);
    MenuItemResponse updateAvailability(Long restaurantId, Long itemId, MenuItemAvailabilityRequest request, Long userId);
    void deleteItem(Long restaurantId, Long itemId, Long userId);

    RestaurantMenuResponse getMenu(Long restaurantId);
}
