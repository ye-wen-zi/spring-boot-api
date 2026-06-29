package com.example.storefront.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
