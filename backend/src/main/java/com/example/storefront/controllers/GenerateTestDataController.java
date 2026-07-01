package com.example.storefront.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.dto.AuthSignupRequest;
import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductUpdateRequest;
import com.example.storefront.services.FakeDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/generation")
@Tag(name = "Generation", description = "API related to generating test data.")
public class GenerateTestDataController {
    private final FakeDataService fakeDataService;

    @GetMapping("/user")
    @Operation(summary = "Generate sign up data")
    public AuthSignupRequest createAuthSignupRequest() {
        return this.fakeDataService.generateAuthSignupData();
    }

    @GetMapping("/product-create")
    @Operation(summary = "Generate a new product to test the product creation API.")
    public ProductCreateRequest createProductCreateRequest() {
        return this.fakeDataService.generateFakeProductRequest();
    }

    @GetMapping("/product-update")
    @Operation(summary = "Get a product to test the product updation API.")
    public ProductUpdateRequest createProductUpdateResuest() {
        return this.fakeDataService.generateProductUpdateRequest();
    }
}
