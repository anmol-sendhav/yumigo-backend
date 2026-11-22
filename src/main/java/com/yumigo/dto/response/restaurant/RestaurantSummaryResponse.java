package com.yumigo.dto.response.restaurant;

import java.math.BigDecimal;
import java.time.LocalTime;

public record RestaurantSummaryResponse(
        int id,
        String name,
        String cuisineType,
        String city,
        BigDecimal avgRating,
        boolean open,
        BigDecimal minOrderAmount,
        LocalTime openingTime,
        LocalTime closingTime
) {
}
