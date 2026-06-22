package com.example.storefront.dto;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String slug,
        String description,
        Double rating,
        String thumbnail,
        Double minPrice,
        Double maxPrice,
        String currency,
        Category category) {

    public record Category(
            UUID id,
            String name,
            String slug) {
    }
}
