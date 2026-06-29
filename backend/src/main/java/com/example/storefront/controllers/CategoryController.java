package com.example.storefront.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.example.storefront.dto.CategoryCreateRequest;
import com.example.storefront.dto.CategoryResponse;
import com.example.storefront.mappers.CategoryMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "APIs related to category management.")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @Operation(summary = "Get product's type.")
    public List<CategoryResponse> find() {
        return this.categoryMapper.fromEntityListToResponseList(this.categoryService.find());
    }

    @PostMapping
    @Operation(summary = "Create new product's type.")
    public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest dto) {
        return this.categoryMapper.fromEntityToResponse(this.categoryService.create(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Detele product's type by id.")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        this.categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
