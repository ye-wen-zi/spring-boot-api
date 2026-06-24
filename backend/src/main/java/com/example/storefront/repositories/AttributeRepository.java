package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.storefront.entity.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, UUID> {

}
