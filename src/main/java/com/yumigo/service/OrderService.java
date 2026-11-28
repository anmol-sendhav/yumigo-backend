package com.yumigo.service;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.order.PlaceOrderRequest;
import com.yumigo.dto.response.order.OrderResponse;
import com.yumigo.dto.response.order.OrderSummaryResponse;

public interface OrderService {
    OrderResponse placeOrder(Long customerId, PlaceOrderRequest request);
    PageResponse<OrderSummaryResponse> getMyOrders(Long customerId, int page, int size);
    OrderResponse getMyOrder(Long customerId, Long orderId);
}