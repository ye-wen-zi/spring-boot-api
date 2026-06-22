package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entity.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {

}
