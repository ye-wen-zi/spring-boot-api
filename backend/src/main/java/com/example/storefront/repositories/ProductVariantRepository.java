package com.example.storefront.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.storefront.entities.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    @EntityGraph(attributePaths = { "assignedAttributes", "assignedAttributes.attribute", "assignedAttributes.value" })
    public List<ProductVariant> findByIdIn(List<UUID> variantIds); // naming convention

    @EntityGraph(attributePaths = {
            "assignedAttributes",
            "assignedAttributes.attribute",
            "assignedAttributes.value"
    })
    @Query("SELECT v FROM ProductVariant v WHERE v.id IN :variantIds")
    List<ProductVariant> findVariantsWithAttributesByIds(@Param("variantIds") List<UUID> variantIds);  // custom name

    @Query("SELECT DISTINCT v FROM ProductVariant v " +
            "LEFT JOIN FETCH v.assignedAttributes aa " +
            "LEFT JOIN FETCH aa.attribute " +
            "LEFT JOIN FETCH aa.value " +
            "WHERE v.id IN :variantIds")
    List<ProductVariant> fetchAttributesForVariants(@Param("variantIds") List<UUID> variantIds); // vẫn như 2 hàm ở trên
}
