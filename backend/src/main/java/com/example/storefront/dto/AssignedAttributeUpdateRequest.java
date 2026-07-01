package com.example.storefront.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record AssignedAttributeUpdateRequest(
        @NotNull(message = "Assigned Attribute ID is required for update operation") UUID assignedId,

        @NotNull(message = "Attribute ID is required") UUID attributeId,

        @NotNull(message = "Attribute value ID is required") UUID attributeValueId) {

}
