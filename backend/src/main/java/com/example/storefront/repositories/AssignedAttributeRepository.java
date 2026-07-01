package com.example.storefront.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.AssignedVariantAttribute;

public interface AssignedAttributeRepository extends JpaRepository<AssignedVariantAttribute, UUID> {
    @EntityGraph(attributePaths = { "attribute", "value" })
    List<AssignedVariantAttribute> findWithAttributeAndValueByVariant_IdIn(List<UUID> variantIds);
}
