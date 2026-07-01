package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.storefront.constants.ProductStatus;

import lombok.Builder;


@Builder(toBuilder = true)
public record ProductVariantResponse(
        UUID id,
        BigDecimal price,
        Integer quantity,
        String sku,
        String name,
        ProductStatus status,
        List<AssignedAttributeResponse> attributes) {
}