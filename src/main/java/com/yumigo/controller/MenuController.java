package com.yumigo.controller;

import com.yumigo.dto.request.menu.MenuCategoryRequest;
import com.yumigo.dto.request.menu.MenuItemAvailabilityRequest;
import com.yumigo.dto.request.menu.MenuItemRequest;
import com.yumigo.dto.response.menu.MenuCategoryResponse;
import com.yumigo.dto.response.menu.MenuItemResponse;
import com.yumigo.dto.response.menu.RestaurantMenuResponse;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // ---------- categories ----------
    @PostMapping("/categories")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<MenuCategoryResponse> createCategory(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuService.createCategory(restaurantId, request, principal.getId()));
    }

    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<MenuCategoryResponse> updateCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long categoryId,
            @Valid @RequestBody MenuCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(
                menuService.updateCategory(restaurantId, categoryId, request, principal.getId()));
    }

    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        menuService.deleteCategory(restaurantId, categoryId, principal.getId());
        return ResponseEntity.noContent().build();
    }

    // ---------- items ----------
    @PostMapping("/items")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<MenuItemResponse> createItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuService.createItem(restaurantId, request, principal.getId()));
    }

    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<MenuItemResponse> updateItem(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId,
            @Valid @RequestBody MenuItemRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(
                menuService.updateItem(restaurantId, itemId, request, principal.getId()));
    }

    @PatchMapping("/items/{itemId}/availability")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<MenuItemResponse> updateAvailability(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId,
            @Valid @RequestBody MenuItemAvailabilityRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(
                menuService.updateAvailability(restaurantId, itemId, request, principal.getId()));
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        menuService.deleteItem(restaurantId, itemId, principal.getId());
        return ResponseEntity.noContent().build();
    }

    // ---------- public menu ----------
    @GetMapping("/menu")
    public ResponseEntity<RestaurantMenuResponse> getMenu(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.getMenu(restaurantId));
    }
}
