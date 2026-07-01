package com.example.storefront.dto;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryResponse(
        UUID id,
        String name,
        String slug) {
}
