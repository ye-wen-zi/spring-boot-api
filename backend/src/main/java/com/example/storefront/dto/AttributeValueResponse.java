package com.example.storefront.dto;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record AttributeValueResponse(
        UUID id,
        String name,
        String slug) {
}