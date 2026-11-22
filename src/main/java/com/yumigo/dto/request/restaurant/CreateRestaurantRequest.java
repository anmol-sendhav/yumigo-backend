package com.yumigo.dto.request.restaurant;

import com.yumigo.constant.ValidationPatterns;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;

public record CreateRestaurantRequest(

        @NotBlank(message = "Enter a valid name")
        @Size(max=150)
        String name,

        @Size(max=500)
        String description,

        @NotBlank(message = "Enter a valid 10-digit phone number")
        @Pattern(regexp = ValidationPatterns.PHONE,message = "phone number is required")
        String phone,

        @NotBlank(message = "Cuisine type is required")
        @Size(max=100)
        String cuisineType,

        @NotBlank(message = "Enter a valid addressLine")
        @Size(max=255)
        String addressLine,

        @NotBlank(message = "Enter a valid city")
        @Size(max=80)
        String city,

        @NotBlank(message = "State is required")
        @Size(max=80)
        String state,

        @NotBlank(message = "Pin code must be 6 digits")
        @Pattern(regexp = "^\\d{6}$")
        String pinCode,

        @NotNull(message = "Enter a valid openingTime")
        LocalTime openingTime,

        @NotNull(message = "Enter a valid ClosingTime")
        LocalTime closingTime,

        BigDecimal latitude,

        BigDecimal longitude,

        @NotNull(message = "Minimum order amount is required")
        @PositiveOrZero(message = "Minimum order amount cannot be negative")
        @Digits(integer = 8, fraction = 2, message = "Maximum 8 digits and 2 decimal places allowed")
        BigDecimal minOrderAmount
) {
}
