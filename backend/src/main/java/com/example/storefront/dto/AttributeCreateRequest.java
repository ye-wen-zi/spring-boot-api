package com.example.storefront.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record AttributeCreateRequest(
        @NotBlank String name,
        List<String> values) {
}
