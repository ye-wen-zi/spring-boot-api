package com.example.storefront.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long id);
}
