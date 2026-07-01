package com.example.storefront.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.storefront.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productType")
    List<Product> findAllWithProductType();

    boolean existsByCategoryId(UUID categoryId);

    boolean existsByProductTypeId(UUID categoryId);

    Optional<Product> findFirstBy();

    @EntityGraph(attributePaths = { "category", "productType" })
    Optional<Product> findWithCategoryAndTypeById(Long id);

    @EntityGraph(attributePaths = { "category", "productType", "variants" })
    Optional<Product> findFirstWithCategoryAndTypeAndVariantsBy();

    @EntityGraph(attributePaths = { "category", "productType", "variants" })
    Optional<Product> findWithCategoryAndTypeAndVariantsById(Long id);
}
