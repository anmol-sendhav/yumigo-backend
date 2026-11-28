package com.yumigo.controller;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.order.CancelOrderRequest;
import com.yumigo.dto.request.order.PlaceOrderRequest;
import com.yumigo.dto.response.order.OrderResponse;
import com.yumigo.dto.response.order.OrderSummaryResponse;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(principal.getId(), request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderSummaryResponse>> myOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal.getId(), page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> myOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(orderService.getMyOrder(principal.getId(), id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) CancelOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(orderService.cancel(principal.getId(), id, request));
    }
}