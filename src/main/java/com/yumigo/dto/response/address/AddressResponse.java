package com.yumigo.dto.response.address;

import java.math.BigDecimal;

public record AddressResponse(
        Long id,
        String label,
        String line1,
        String line2,
        String city,
        String state,
        String pinCode,
        BigDecimal latitude,
        BigDecimal longitude,
        boolean defaultAddress

        ) {
}
