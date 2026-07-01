package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.storefront.constants.ProductStatus;
import com.example.storefront.serializers.HashidsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;

@Builder(toBuilder = true)
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
        List<ProductVariantResponse> variants) {
}