package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.storefront.constants.ProductStatus;
import com.example.storefront.serializers.HashidsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record ProductDetailResponse(
                @JsonSerialize(using = HashidsSerializer.class) Long id,
                String name,
                String slug,
                String description,
                Double rating,
                String categoryName,
                String productTypeName,
                String currency,
                BigDecimal minPrice,
                BigDecimal maxPrice,
                String thumbnail,
                ProductStatus status,
                List<VariantResponse> variants) {

        public record VariantResponse(
                        UUID id,
                        BigDecimal price,
                        Integer quantity,
                        String sku,
                        String name,
                        List<AttributeValueResponse> attributes) {
        }

        public record AttributeValueResponse(
                        String attributeName,
                        String valueName) {
        }
}