package com.yumigo.dto.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

        @Size(min=-90,max=90)
        BigDecimal latitude,

        @Size(min=-180,max=180)
        BigDecimal longitude,

        boolean defaultAddress
) {
}
