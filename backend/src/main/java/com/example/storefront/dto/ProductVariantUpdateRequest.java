package com.example.storefront.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.storefront.constants.ProductStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record ProductVariantUpdateRequest(
        @NotNull(message = "Variant ID is required for update operation") UUID id,

        @NotNull(message = "Variant price is required") @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0.0") BigDecimal price,

        @Min(value = 0, message = "Quantity cannot be less than 0") int quantity,

        @NotBlank(message = "SKU is required") @Size(max = 100, message = "SKU must not exceed 100 characters") String sku,

        @NotBlank(message = "Variant name is required") @Size(max = 255, message = "Variant name must not exceed 255 characters") String name,

        boolean trackInventory,

        @NotNull(message = "Variant status is required") ProductStatus status,

        @Valid List<AssignedAttributeUpdateRequest> attributes) {
}
