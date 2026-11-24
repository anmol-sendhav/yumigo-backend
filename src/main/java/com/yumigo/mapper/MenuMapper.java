package com.yumigo.mapper;

import com.yumigo.dto.request.menu.MenuCategoryRequest;
import com.yumigo.dto.request.menu.MenuItemRequest;
import com.yumigo.dto.response.menu.MenuCategoryResponse;
import com.yumigo.dto.response.menu.MenuItemResponse;
import com.yumigo.entity.MenuCategory;
import com.yumigo.entity.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

        public MenuCategoryResponse toCategoryResponse(MenuCategory category) {
            return new MenuCategoryResponse(
                    category.getId(),
                    category.getName(),
                    category.getDisplayOrder()
            );
        }

        public MenuItemResponse toItemResponse(MenuItem item) {
            return new MenuItemResponse(
                    item.getId(),
                    item.getCategory().getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.isVeg(),
                    item.isAvailable(),
                    item.getImageUrl(),
                    item.getPreparationTimeMins()
            );
        }

    public void applyToCategory(MenuCategory target, MenuCategoryRequest request) {
        target.setName(request.name().trim());
        target.setDisplayOrder(request.displayOrder());
    }

    public void applyToItem(MenuItem target, MenuItemRequest request) {
        target.setName(request.name().trim());
        target.setDescription(request.description());
        target.setPrice(request.price());
        target.setVeg(request.veg());
        target.setImageUrl(request.imageUrl());
        target.setPreparationTimeMins(request.preparationTimeMins());
        if (request.available() != null) {
            target.setAvailable(request.available());
        }
    }
    }
