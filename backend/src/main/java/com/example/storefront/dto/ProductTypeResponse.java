package com.example.storefront.dto;

import java.util.UUID;

import lombok.Builder;


@Builder(toBuilder = true)
public record ProductTypeResponse(
        UUID id,
        String name) {

}
