package com.example.storefront.dto;

import com.example.storefront.constants.ProductStatus;
import com.example.storefront.serializers.HashidsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;

@Builder(toBuilder = true)
public record ProductResponse(
        @JsonSerialize(using = HashidsSerializer.class) Long id,
        String name,
        String slug,
        String description,
        Double rating,
        String thumbnail,
        Double minPrice,
        Double maxPrice,
        String currency,
        ProductStatus status
// CategoryResponse category
) {
}
