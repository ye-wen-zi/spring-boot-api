package com.example.storefront.dto;

import java.util.List;

import lombok.Builder;

@Builder(toBuilder = true)
public record CartResponse(
        Long id,
        List<CartItemResponse> items) {
}