package com.yumigo.dto.response.order;

public record DeliveryAddressResponse(
        String line1,
        String line2,
        String city,
        String state,
        String pinCode) {}

