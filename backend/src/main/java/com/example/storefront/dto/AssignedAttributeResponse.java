package com.example.storefront.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record AssignedAttributeResponse(
        String attributeName,
        String valueName) {
}
