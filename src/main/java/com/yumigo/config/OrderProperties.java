package com.yumigo.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "yumigo.order")
@Validated
public record OrderProperties(
        @NotNull BigDecimal deliveryFee,
        @NotNull BigDecimal taxPercent
) {
}