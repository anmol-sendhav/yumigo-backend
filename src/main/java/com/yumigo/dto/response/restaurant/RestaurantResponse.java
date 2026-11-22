package com.yumigo.dto.response.restaurant;

import com.yumigo.enums.RestaurantStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record RestaurantResponse(
        Long id,
        String name,
        String description,
        String phone,
        String cuisineType,
        String addressLine,
        String city,
        String state,
        String pinCode,
        LocalTime openingTime,
        LocalTime closingTime,
        boolean open,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal minOrderAmount,
        BigDecimal avgRating,
        RestaurantStatus status,
        Long ownerId,
        LocalDateTime createdAt
) {}
