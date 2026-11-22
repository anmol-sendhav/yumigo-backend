package com.yumigo.controller;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.restaurant.CreateRestaurantRequest;
import com.yumigo.dto.request.restaurant.RestaurantStatusUpdateRequest;
import com.yumigo.dto.response.restaurant.RestaurantResponse;
import com.yumigo.dto.response.restaurant.RestaurantSummaryResponse;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<RestaurantResponse> create(
            @Valid @RequestBody CreateRestaurantRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.create(request, principal.getId()));
    }

    @GetMapping
    public ResponseEntity<PageResponse<RestaurantSummaryResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String cuisineType,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                restaurantService.search(city, cuisineType, name, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<List<RestaurantResponse>> myRestaurants(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(restaurantService.getMyRestaurants(principal.getId()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantStatusUpdateRequest request) {
        return ResponseEntity.ok(restaurantService.updateStatus(id, request));
    }
}