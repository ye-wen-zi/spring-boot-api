package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entity.AttributeValue;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, UUID> {

}
