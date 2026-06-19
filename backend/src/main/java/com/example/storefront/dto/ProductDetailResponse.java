package com.example.storefront.dto;

import java.util.List;
import java.util.UUID;

public record ProductDetailResponse(
        UUID id,
        String name,
        String slug,
        String description,
        Double rating,
        String categoryName,
        String productTypeName,
        List<VariantResponse> variants) {
    public record VariantResponse(
            UUID id,
            String sku,
            String name,
            List<AttributeValueResponse> attributes) {
    }

    public record AttributeValueResponse(
            String attributeName,
            String valueName) {
    }
}