package com.example.storefront.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryCreateRequest(
        @NotBlank String name) {

}
