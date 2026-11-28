package com.yumigo.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CartData {
    private Long restaurantId;
    private Map<Long, Integer> items = new LinkedHashMap<>();   // menuItemId -> quantity
}
