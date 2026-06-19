package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {

}
