package com.yumigo.service;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.order.CancelOrderRequest;
import com.yumigo.dto.request.order.PlaceOrderRequest;
import com.yumigo.dto.request.order.UpdateOrderStatusRequest;
import com.yumigo.dto.response.order.OrderResponse;
import com.yumigo.dto.response.order.OrderSummaryResponse;
import com.yumigo.enums.OrderStatus;

public interface OrderService {
    OrderResponse placeOrder(Long customerId, PlaceOrderRequest request);

    PageResponse<OrderSummaryResponse> getMyOrders(Long customerId, int page, int size);

    OrderResponse getMyOrder(Long customerId, Long orderId);

    PageResponse<OrderSummaryResponse> getOwnerOrders(Long ownerId, OrderStatus status, int page, int size);

    OrderResponse getOwnerOrder(Long ownerId, Long orderId);

    OrderResponse updateStatus(Long ownerId, Long orderId, UpdateOrderStatusRequest request);

    OrderResponse cancel(Long customerId, Long orderId, CancelOrderRequest request);
}