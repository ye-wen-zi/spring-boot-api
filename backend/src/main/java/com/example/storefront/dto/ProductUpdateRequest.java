package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.storefront.constants.ProductStatus;

public record ProductUpdateRequest(
        String name,
        // String slug,
        String description,
        UUID typeId,
        UUID categoryId,
        double rating,
        String thumbnail,
        // double minPrice,
        // double maxPrice,
        ProductStatus status,
        List<String> images,
        String currency,
        List<Variant> variants) {

    public record Variant(
            UUID id,
            BigDecimal price,
            int quantity,
            String sku,
            String name,
            boolean trackInventory,
            ProductStatus status,
            List<Attribute> attributes) {
        public record Attribute(
                UUID assignedId,
                UUID attributeId,
                UUID attributeValueId) {
        }
    }
}
