package com.yumigo.dto.request.restaurant;

import jakarta.validation.constraints.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

public record RestaurantSearchCriteria(
        String city,
        String cuisineType,
        String name,
        boolean openNow,
        @DecimalMin("0.0")
        @DecimalMax("5.0")
        BigDecimal minRating,
        @Min(0) Integer page,
        @Min(1) @Max(50) Integer size,
        @Pattern(regexp = "name|avgRating|city|minOrderAmount|createdAt",
                message = "sortBy must be one of: name, avgRating, city, minOrderAmount, createdAt")
        String sortBy,
        @Pattern(regexp = "(?i)asc|desc", message = "direction must be asc or desc")
        String direction

) {
    public RestaurantSearchCriteria {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortBy == null) sortBy = "avgRating";
        if (direction == null) direction = "desc";
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(direction), sortBy));
    }
}