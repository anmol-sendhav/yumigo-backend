package com.yumigo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
@Entity
public class Address extends BaseEntity{

    @Column(nullable = false,length = 30)
    private String label;
    @Column(nullable = false,length = 255)
    private String line1;
    private String line2;
    @Column(length=80,nullable = false)
    private String city;
    @Column(length=80,nullable = false)
    private String state;
    @Column(length = 6,nullable = false,name="pin_code")
    private String pinCode;
    @Column(name = "is_default", nullable = false)
    private boolean defaultAddress;
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
