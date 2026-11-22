package com.yumigo.repository;

import com.yumigo.entity.Restaurant;
import com.yumigo.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    @Query("""

            select r from Restaurant r
    where r.status = com.yumigo.enums.RestaurantStatus.ACTIVE
      and (:city is null or r.city = :city)
    """)
    Page<Restaurant> search(@Param("city") String city, Pageable pageable);

    List<Restaurant> findByOwnerId(Long ownerId);

    Optional<Restaurant> findByIdAndStatus(Long id, RestaurantStatus status);

    @Query("""
        select r from Restaurant r
        where r.status = com.yumigo.enums.RestaurantStatus.ACTIVE
          and (:city is null or r.city = :city)
          and (:cuisineType is null or r.cuisineType = :cuisineType)
          and (:name is null or lower(r.name) like lower(concat('%', :name, '%')))
        """)
    Page<Restaurant> searchActive(@Param("city") String city,
                                  @Param("cuisineType") String cuisineType,
                                  @Param("name") String name,
                                  Pageable pageable);


}