package com.example.storefront.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductTypeCreateRequest(
        @NotBlank String name) {

}
