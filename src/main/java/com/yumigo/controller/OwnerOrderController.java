package com.yumigo.controller;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.order.UpdateOrderStatusRequest;
import com.yumigo.dto.response.order.OrderResponse;
import com.yumigo.dto.response.order.OrderSummaryResponse;
import com.yumigo.enums.OrderStatus;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RESTAURANT_OWNER')")
public class OwnerOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PageResponse<OrderSummaryResponse>> list(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(orderService.getOwnerOrders(principal.getId(), status, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(orderService.getOwnerOrder(principal.getId(), id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(orderService.updateStatus(principal.getId(), id, request));
    }
}
