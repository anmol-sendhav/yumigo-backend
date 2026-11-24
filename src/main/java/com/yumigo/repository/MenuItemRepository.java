package com.yumigo.repository;

import com.yumigo.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {

    @Query("""
            select i from MenuItem i
            join fetch i.category
            where i.restaurant.id = :restaurantId
            order by i.name
            """)
    List<MenuItem> findAllByRestaurantIdWithCategory(@Param("restaurantId") Long restaurantId);

    Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);

    boolean existsByCategoryId(Long categoryId);
}
