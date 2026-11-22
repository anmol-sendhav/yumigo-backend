package com.yumigo.entity;

import com.yumigo.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurants", indexes = {
        @Index(name = "idx_restaurants_city", columnList = "city"),
        @Index(name = "idx_restaurants_status", columnList = "status")
})
@Entity
public class Restaurant  extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @Column(length=500,nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(length=500,nullable = false)
    private String phone;
    @Column(length=500,nullable = false)
    private String city;
    @Column(name = "cuisine_type", length = 100, nullable = false)
    private String cuisineType;
    @Column(name = "address_line",nullable = false)
    private String addressLine;
    @Column(length=80,nullable = false)
    private String state;
    @Column(length = 6,nullable = false,name="pin_code")
    private String pinCode;
    @Column(name = "opening_time",nullable = false)
    private LocalTime openingTime;
    @Column(name = "closing_time",nullable = false)
    private LocalTime closingTime;
    @Column(name = "is_open", nullable = false)
    private boolean open;
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;
    @Column(precision = 10, scale = 2,name = "min_order_amount",nullable = false)
    private BigDecimal minOrderAmount;
    @Column(precision = 2, scale = 1,name = "avg_rating",nullable = false)
    private BigDecimal avgRating;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private RestaurantStatus status;
}
