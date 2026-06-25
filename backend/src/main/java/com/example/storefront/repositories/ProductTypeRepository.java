package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.storefront.entities.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {

}
