package com.yumigo.dto.request.address;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AddressRequest(

        @NotBlank(message = "Please Enter a label")
        @Size(max = 30)
        String label,

        @NotBlank(message = "Must be fill the line 1")
        @Size(max=255)
        String line1,

        String line2,

        @NotBlank(message = "Enter a valid City")
        @Size(max=80)
        String city,

        @NotBlank(message = "Enter a valid State")
        @Size(max=80)
        String state,

        @NotBlank(message = "Enter a valid City")
        @Pattern(regexp = "^\\d{6}$")
        String pinCode,

        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        BigDecimal latitude,

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        BigDecimal longitude,

        boolean defaultAddress
) {
}
