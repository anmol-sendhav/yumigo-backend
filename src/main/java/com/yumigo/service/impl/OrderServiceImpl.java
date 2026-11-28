package com.yumigo.service.impl;

import com.yumigo.cart.CartData;
import com.yumigo.cart.CartStore;
import com.yumigo.config.OrderProperties;
import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.order.CancelOrderRequest;
import com.yumigo.dto.request.order.PlaceOrderRequest;
import com.yumigo.dto.request.order.UpdateOrderStatusRequest;
import com.yumigo.dto.response.order.OrderResponse;
import com.yumigo.dto.response.order.OrderSummaryResponse;
import com.yumigo.entity.*;
import com.yumigo.enums.OrderStatus;
import com.yumigo.enums.PaymentMethod;
import com.yumigo.enums.PaymentStatus;
import com.yumigo.enums.RestaurantStatus;
import com.yumigo.exception.BadRequestException;
import com.yumigo.exception.ResourceNotFoundException;
import com.yumigo.mapper.OrderMapper;
import com.yumigo.repository.*;
import com.yumigo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Set<PaymentMethod> SUPPORTED_PAYMENT_METHODS = Set.of(PaymentMethod.COD);

    private final CartStore cartStore;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final OrderMapper orderMapper;
    private final OrderProperties props;

    @Override
    @Transactional
    public OrderResponse placeOrder(Long customerId, PlaceOrderRequest request) {

        // 1. payment method we can actually process
        if (!SUPPORTED_PAYMENT_METHODS.contains(request.paymentMethod())) {
            throw new BadRequestException("Only cash on delivery is available right now");
        }

        // 2. cart
        CartData cart = cartStore.find(customerId)
                .filter(c -> !c.getItems().isEmpty())
                .orElseThrow(() -> new BadRequestException("Your cart is empty"));

        // 3. customer, address (must be the customer's own), restaurant
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", customerId));

        Address address = addressRepository.findByIdAndUserId(request.addressId(), customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", request.addressId()));

        Restaurant restaurant = restaurantRepository
                .findByIdAndStatus(cart.getRestaurantId(), RestaurantStatus.ACTIVE)
                .orElseThrow(() -> new BadRequestException("This restaurant is not accepting orders"));

        if (!restaurant.isOpen()) {
            throw new BadRequestException("This restaurant is currently closed");
        }

        // 4. load all menu items in ONE query
        Map<Long, MenuItem> menuItems = menuItemRepository.findAllById(cart.getItems().keySet())
                .stream().collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        CustomerOrder order = new CustomerOrder();
        BigDecimal subtotal = BigDecimal.ZERO;

        // 5. build order lines with price SNAPSHOTS
        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            MenuItem menuItem = menuItems.get(entry.getKey());

            if (menuItem == null || !menuItem.isAvailable()
                    || !menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new BadRequestException(
                        "An item in your cart is no longer available. Please review your cart.");
            }

            int quantity = entry.getValue();
            BigDecimal lineTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));

            OrderItem line = new OrderItem();
            line.setMenuItem(menuItem);
            line.setItemName(menuItem.getName());
            line.setUnitPrice(menuItem.getPrice());
            line.setQuantity(quantity);
            line.setLineTotal(lineTotal);
            order.addItem(line);

            subtotal = subtotal.add(lineTotal);
        }

        // 6. minimum order amount
        if (subtotal.compareTo(restaurant.getMinOrderAmount()) < 0) {
            throw new BadRequestException(
                    "Minimum order amount is " + restaurant.getMinOrderAmount());
        }

        // 7. money: always calculated on the server
        BigDecimal deliveryFee = props.deliveryFee();
        BigDecimal tax = subtotal.multiply(props.taxPercent())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = subtotal.add(deliveryFee).add(tax).subtract(discount);

        // 8. fill the order
        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);
        order.setContactPhone(customer.getPhone());
        order.setDeliveryLine1(address.getLine1());
        order.setDeliveryLine2(address.getLine2());
        order.setDeliveryCity(address.getCity());
        order.setDeliveryState(address.getState());
        order.setDeliveryPinCode(address.getPinCode());
        order.setDeliveryLatitude(address.getLatitude());
        order.setDeliveryLongitude(address.getLongitude());
        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);
        order.setTax(tax);
        order.setDiscount(discount);
        order.setTotalAmount(total);
        order.setPaymentMethod(request.paymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setSpecialInstructions(request.specialInstructions());
        order.setPlacedAt(LocalDateTime.now());

        CustomerOrder saved = orderRepository.save(order);   // cascades to the items

        // 9. first history row
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(saved);
        history.setFromStatus(null);
        history.setToStatus(OrderStatus.PLACED);
        history.setChangedBy(customer);
        history.setRemark("Order placed");
        historyRepository.save(history);

        // 10. the cart has become an order
        cartStore.delete(customerId);

        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getMyOrders(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return PageResponse.from(
                orderRepository.findByCustomerIdOrderByPlacedAtDesc(customerId, pageable)
                        .map(orderMapper::toSummary));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getMyOrder(Long customerId, Long orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
    }

    private static final Map<OrderStatus, Set<OrderStatus>> OWNER_TRANSITIONS = Map.of(
            OrderStatus.PLACED,    Set.of(OrderStatus.ACCEPTED, OrderStatus.REJECTED),
            OrderStatus.ACCEPTED,  Set.of(OrderStatus.PREPARING),
            OrderStatus.PREPARING, Set.of(OrderStatus.READY),
            OrderStatus.READY,     Set.of(OrderStatus.PICKED_UP),
            OrderStatus.PICKED_UP, Set.of(OrderStatus.DELIVERED)
    );

    private static final Set<OrderStatus> CUSTOMER_CANCELLABLE =
            Set.of(OrderStatus.PLACED, OrderStatus.ACCEPTED);

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getOwnerOrders(Long ownerId, OrderStatus status,
                                                             int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50),
                Sort.by("placedAt").descending());
        return PageResponse.from(
                orderRepository.findForOwner(ownerId, status, pageable).map(orderMapper::toSummary));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOwnerOrder(Long ownerId, Long orderId) {
        return orderRepository.findByIdAndRestaurantOwnerId(orderId, ownerId)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long ownerId, Long orderId, UpdateOrderStatusRequest request) {
        CustomerOrder order = orderRepository.findByIdAndRestaurantOwnerId(orderId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        OrderStatus current = order.getStatus();
        OrderStatus target = request.status();

        if (!OWNER_TRANSITIONS.getOrDefault(current, Set.of()).contains(target)) {
            throw new BadRequestException("Cannot change order from " + current + " to " + target);
        }

        applyStatus(order, target, userRepository.getReferenceById(ownerId), request.remark());
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(Long customerId, Long orderId, CancelOrderRequest request) {
        CustomerOrder order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (!CUSTOMER_CANCELLABLE.contains(order.getStatus())) {
            throw new BadRequestException(
                    "This order can no longer be cancelled (status: " + order.getStatus() + ")");
        }

        String reason = (request == null) ? null : request.reason();
        applyStatus(order, OrderStatus.CANCELLED, userRepository.getReferenceById(customerId), reason);
        return orderMapper.toResponse(order);
    }

    private void applyStatus(CustomerOrder order, OrderStatus target, User actor, String remark) {
        OrderStatus from = order.getStatus();
        order.setStatus(target);

        if (target == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
            order.setPaymentStatus(PaymentStatus.PAID);          // cash collected on delivery
        }
        if (target == OrderStatus.REJECTED || target == OrderStatus.CANCELLED) {
            order.setCancelReason(remark);
        }

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(from);
        history.setToStatus(target);
        history.setChangedBy(actor);
        history.setRemark(remark);
        historyRepository.save(history);
    }
    private String generateOrderNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "YMG-" + date + "-" + random;
    }
}
