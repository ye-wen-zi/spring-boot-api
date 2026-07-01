package com.example.storefront.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
    // "FROM Product p WHERE p.category.id = :categoryId")
    // boolean hasProducts(@Param("categoryId") UUID categoryId);

    Optional<Category> findFirstBy();
}
