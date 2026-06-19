package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

}
