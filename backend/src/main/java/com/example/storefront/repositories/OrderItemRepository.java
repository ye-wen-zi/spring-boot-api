package com.example.storefront.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByProductVariantProductId(Long id);
}
