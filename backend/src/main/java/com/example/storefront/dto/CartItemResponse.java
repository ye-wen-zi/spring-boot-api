package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record CartItemResponse(
        Long id,
        Long productId,
        UUID variantId,
        String sku,
        String productName,
        String variantName,
        String productSlug,
        String thumbnailUrl,
        BigDecimal unitPrice,
        Integer quantity,
        Integer remaining,
        List<AssignedAttributeResponse> attributes) {
}