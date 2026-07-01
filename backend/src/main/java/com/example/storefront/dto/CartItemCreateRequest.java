package com.example.storefront.dto;

import java.util.UUID;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Min;
import lombok.Builder;


@Builder(toBuilder = true)
public record CartItemCreateRequest(
        @NonNull UUID variantId,
        @Min(value = 0, message = "Quantity cannot be less than 0") int quantity) {

}
