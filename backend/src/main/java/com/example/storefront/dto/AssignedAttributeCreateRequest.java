package com.example.storefront.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record AssignedAttributeCreateRequest(
        @NotNull(message = "Attribute ID is required") UUID attributeId,
        @NotNull(message = "Attribute value ID is required") UUID attributeValueId) {

}
