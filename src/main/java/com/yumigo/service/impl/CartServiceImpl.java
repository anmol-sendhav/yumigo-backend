package com.yumigo.service.impl;

import com.yumigo.cart.CartData;
import com.yumigo.cart.CartStore;
import com.yumigo.dto.request.cart.AddToCartRequest;
import com.yumigo.dto.request.cart.UpdateCartItemRequest;
import com.yumigo.dto.response.cart.CartItemResponse;
import com.yumigo.dto.response.cart.CartResponse;
import com.yumigo.entity.MenuItem;
import com.yumigo.entity.Restaurant;
import com.yumigo.enums.RestaurantStatus;
import com.yumigo.exception.BadRequestException;
import com.yumigo.exception.ResourceNotFoundException;
import com.yumigo.repository.MenuItemRepository;
import com.yumigo.repository.RestaurantRepository;
import com.yumigo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartStore cartStore;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private static final int MAX_QUANTITY_PER_ITEM = 20;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {

        CartData cart = cartStore.find(userId).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            return emptyCart();
        }

        // one query for all items in the cart (not one per item)
        Map<Long, MenuItem> itemsById = menuItemRepository.findAllById(cart.getItems().keySet())
                .stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        String restaurantName = restaurantRepository.findById(cart.getRestaurantId())
                .map(Restaurant::getName)
                .orElse(null);

        List<CartItemResponse> lines = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int itemCount = 0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            MenuItem item = itemsById.get(entry.getKey());
            if (item == null) {
                continue;                      // item was deleted from the menu
            }
            int quantity = entry.getValue();
            BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(quantity));

            lines.add(new CartItemResponse(
                    item.getId(), item.getName(), item.getPrice(),
                    quantity, lineTotal, item.isAvailable()));

            itemCount += quantity;
            if (item.isAvailable()) {
                subtotal = subtotal.add(lineTotal);
            }
        }
        return new CartResponse(cart.getRestaurantId(), restaurantName, lines, itemCount, subtotal);
    }
    private CartResponse emptyCart() {
        return new CartResponse(null, null, List.of(), 0, BigDecimal.ZERO);
    }


    @Override
    @Transactional
    public CartResponse addItem(Long userId, AddToCartRequest request) {

        // 1. the item must exist
        MenuItem item = menuItemRepository.findById(request.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item", request.menuItemId()));

        // 2. the item must be orderable
        if (!item.isAvailable()) {
            throw new BadRequestException("This item is currently unavailable");
        }

        Restaurant restaurant = item.getRestaurant();
        if (restaurant.getStatus() != RestaurantStatus.ACTIVE) {
            throw new BadRequestException("This restaurant is not accepting orders");
        }

        // 3. load the cart, or start a new one
        CartData cart = cartStore.find(userId).orElseGet(CartData::new);

        // 4. one restaurant per cart
        if (cart.getRestaurantId() != null && !cart.getRestaurantId().equals(restaurant.getId())) {
            throw new BadRequestException(
                    "Your cart has items from another restaurant. Clear it first.");
        }

        // 5. add the quantity and enforce the limit
        int newQuantity = cart.getItems().getOrDefault(item.getId(), 0) + request.quantity();
        if (newQuantity > MAX_QUANTITY_PER_ITEM) {
            throw new BadRequestException(
                    "You can order at most " + MAX_QUANTITY_PER_ITEM + " of the same item");
        }

        cart.setRestaurantId(restaurant.getId());
        cart.getItems().put(item.getId(), newQuantity);

        // 6. save to Redis (this also resets the 3-day TTL)
        cartStore.save(userId, cart);

        // 7. return the updated cart
        return getCart(userId);
    }

    @Override
    public CartResponse updateQuantity(Long userId, Long menuItemId, UpdateCartItemRequest request) {

        CartData cart = cartStore.find(userId).orElseThrow(
        () -> new ResourceNotFoundException("Cart Item",menuItemId));

        if(!cart.getItems().containsKey(menuItemId)){
            throw new ResourceNotFoundException("Cart Item",menuItemId);
        }
        cart.getItems().put(menuItemId,request.quantity());
        cartStore.save(userId,cart);
        return getCart(userId);
    }

    @Override
    public CartResponse removeItem(Long userId, Long menuItemId) {

        CartData cart = cartStore.find(userId).orElseThrow(
        () -> new ResourceNotFoundException("Cart Item",menuItemId));

        if (cart.getItems().remove(menuItemId) == null) {
            throw new ResourceNotFoundException("Cart item", menuItemId);
        }

        if(cart.getItems().isEmpty()){
          cartStore.delete(userId);
          return emptyCart();
        }
        cartStore.save(userId,cart);
        return getCart(userId);
    }

    @Override
    public void clear(Long userId) {
     cartStore.delete(userId);
    }
}
