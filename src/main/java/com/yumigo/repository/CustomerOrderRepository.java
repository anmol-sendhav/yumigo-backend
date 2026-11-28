package com.yumigo.repository;

import com.yumigo.entity.CustomerOrder;
import com.yumigo.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public  interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @EntityGraph(attributePaths = {"items", "restaurant"})
    Optional<CustomerOrder> findByIdAndCustomerId(Long id, Long customerId);

    @EntityGraph(attributePaths = "restaurant")
    Page<CustomerOrder> findByCustomerIdOrderByPlacedAtDesc(Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "restaurant"})
    Optional<CustomerOrder> findByIdAndRestaurantOwnerId(Long id, Long ownerId);

    @Query(value = """
        select o from CustomerOrder o join fetch o.restaurant r
        where r.owner.id = :ownerId
          and (:status is null or o.status = :status)
        """,
            countQuery = """
        select count(o) from CustomerOrder o
        where o.restaurant.owner.id = :ownerId
          and (:status is null or o.status = :status)
        """)
    Page<CustomerOrder> findForOwner(@Param("ownerId") Long ownerId,
                                     @Param("status") OrderStatus status,
                                     Pageable pageable);
}
