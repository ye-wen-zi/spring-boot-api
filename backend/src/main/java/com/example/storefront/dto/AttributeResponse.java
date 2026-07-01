package com.example.storefront.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record AttributeResponse(
        UUID id,
        String name,
        String slug,
        List<AttributeValueResponse> values) {
}
