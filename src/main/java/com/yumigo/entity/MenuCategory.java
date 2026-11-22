package com.yumigo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_categories",
        uniqueConstraints = @UniqueConstraint(name = "uk_menu_categories_restaurant_name",
                columnNames = {"restaurant_id", "name"}))
public class MenuCategory extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @Column(nullable = false,length = 100)
    private String name;
    @Column(nullable = false,name="display_order")
    private int displayOrder;
}