package com.example.storefront.dto;

import java.util.UUID;

public record CartItemRequest(
        UUID variantId,
        int quantity) {

}
