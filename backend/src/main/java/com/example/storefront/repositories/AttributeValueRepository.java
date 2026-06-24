package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.storefront.entity.AttributeValue;


@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, UUID> {

}
