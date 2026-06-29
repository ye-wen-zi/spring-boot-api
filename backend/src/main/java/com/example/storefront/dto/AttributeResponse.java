package com.example.storefront.dto;

import java.util.List;
import java.util.UUID;

public record AttributeResponse(
        UUID id,
        String name,
        String slug,
        List<AttributeValueResponse> values) {
    public record AttributeValueResponse(
            UUID id,
            String name,
            String slug) {
    }
}
