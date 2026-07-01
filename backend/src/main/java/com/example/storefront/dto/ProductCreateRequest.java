package com.example.storefront.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record ProductCreateRequest(
        @NotBlank(message = "Product name is required and cannot be blank") @Size(max = 255, message = "Product name must not exceed 255 characters") String name,

        @Size(max = 2000, message = "Description must not exceed 2000 characters") String description,

        @NotNull(message = "Type ID is required") UUID typeId,

        @NotNull(message = "Category ID is required") UUID categoryId,

        // @PositiveOrZero(message = "Rating must be zero or a positive number")
        // double rating,

        @NotBlank(message = "Thumbnail image URL is required") String thumbnail,

        @NotEmpty(message = "Product must have at least one image") List<@NotBlank(message = "Image URL cannot be blank") String> images,

        @NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code (e.g., USD, VND)") String currency,

        @NotEmpty(message = "Product must have at least one variant") @Valid List<ProductVariantCreateRequest> variants) {
}