package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.AssignedVariantAttribute;

public interface AssignedAttributeRepository extends JpaRepository<AssignedVariantAttribute, UUID> {

}
