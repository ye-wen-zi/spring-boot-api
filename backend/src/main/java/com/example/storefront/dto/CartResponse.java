package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CartResponse(
    Long id,
    List<CartItemResponse> items
) {
    
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
        Integer remaining,     // Trạng thái còn hàng hay không (check từ kho của Variant)
        List<AttributeResponse> attributes
    ) {}

    public record AttributeResponse(
        String attributeName,
        String valueName
    ) {}
}