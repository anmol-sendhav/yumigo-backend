package com.yumigo.repository;

import com.yumigo.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public  interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @EntityGraph(attributePaths = {"items", "restaurant"})
    Optional<CustomerOrder> findByIdAndCustomerId(Long id, Long customerId);

    @EntityGraph(attributePaths = "restaurant")
    Page<CustomerOrder> findByCustomerIdOrderByPlacedAtDesc(Long customerId, Pageable pageable);
}
