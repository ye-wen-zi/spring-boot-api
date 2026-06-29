package com.example.storefront.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.dto.ProductTypeCreateRequest;
import com.example.storefront.dto.ProductTypeResponse;
import com.example.storefront.mappers.ProductTypeMapper;
import com.example.storefront.services.ProductTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/product-types")
@AllArgsConstructor
@Tag(name = "Product's types", description = "APIs related to product's types management.")
public class ProductTypeController {
    private final ProductTypeService productTypeService;
    private final ProductTypeMapper productTypeMapper;

    @GetMapping
    @Operation(summary = "Get product's types")
    public List<ProductTypeResponse> find() {
        return this.productTypeMapper.fromEntityListToResponse(
                this.productTypeService.find());
    }

    @PostMapping()
    @Operation(summary = "Create new product's type")
    public ProductTypeResponse create(@RequestBody ProductTypeCreateRequest dto) {
        return this.productTypeMapper.fromEntityToResponse(
                this.productTypeService.create(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product's type by id")
    public ResponseEntity<?> delete(UUID id) {
        this.productTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
