package com.yumigo.mapper;

import com.yumigo.dto.response.order.DeliveryAddressResponse;
import com.yumigo.dto.response.order.OrderItemResponse;
import com.yumigo.dto.response.order.OrderResponse;
import com.yumigo.dto.response.order.OrderSummaryResponse;
import com.yumigo.entity.CustomerOrder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(CustomerOrder o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getItemName(), i.getUnitPrice(),
                        i.getQuantity(), i.getLineTotal()))
                .toList();

        return new OrderResponse(
                o.getId(), o.getOrderNumber(), o.getStatus(),
                o.getRestaurant().getId(), o.getRestaurant().getName(), items,
                o.getSubtotal(), o.getDeliveryFee(), o.getTax(), o.getDiscount(), o.getTotalAmount(),
                o.getPaymentMethod(), o.getPaymentStatus(),
                new DeliveryAddressResponse(o.getDeliveryLine1(), o.getDeliveryLine2(),
                        o.getDeliveryCity(), o.getDeliveryState(), o.getDeliveryPinCode()),
                o.getContactPhone(), o.getSpecialInstructions(), o.getCancelReason(),
                o.getPlacedAt(), o.getDeliveredAt());
    }

    public OrderSummaryResponse toSummary(CustomerOrder o) {
        return new OrderSummaryResponse(o.getId(), o.getOrderNumber(), o.getStatus(),
                o.getRestaurant().getName(), o.getTotalAmount(), o.getPlacedAt());
    }
}
