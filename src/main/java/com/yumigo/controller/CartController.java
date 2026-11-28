package com.yumigo.controller;

import com.yumigo.dto.request.cart.AddToCartRequest;
import com.yumigo.dto.request.cart.UpdateCartItemRequest;
import com.yumigo.dto.response.cart.CartResponse;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(cartService.getCart(principal.getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @Valid @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(cartService.addItem(principal.getId(), request));
    }

    @PutMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long menuItemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(
                cartService.updateQuantity(principal.getId(), menuItemId, request));
    }

    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long menuItemId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(cartService.removeItem(principal.getId(), menuItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clear(
            @AuthenticationPrincipal CustomUserDetails principal) {
        cartService.clear(principal.getId());
        return ResponseEntity.noContent().build();
    }
}
