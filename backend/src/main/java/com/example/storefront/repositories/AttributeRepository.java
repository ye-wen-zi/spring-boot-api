package com.example.storefront.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.storefront.entities.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {

    @Query("SELECT a FROM Attribute a LEFT JOIN FETCH a.values")
    List<Attribute> findWithValues();
}
