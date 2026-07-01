package com.example.storefront.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {
    // @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
    // "FROM Product p WHERE p.productType.id = :typeId")
    // boolean hasProducts(@Param("typeId") UUID typeId);

    Optional<ProductType> findFirstBy();
}
