package com.example.storefront.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.dto.AttributeCreateRequest;
import com.example.storefront.dto.AttributeResponse;
import com.example.storefront.dto.AttributeValueResponse;
import com.example.storefront.mappers.AttributeMapper;
import com.example.storefront.mappers.AttributeValueMapper;
import com.example.storefront.services.AttributeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/attributes")
@RequiredArgsConstructor
@Tag(name = "Product's attributes", description = "APIs related to product's attributes management.")
public class AttributeController {
    private final AttributeService attributeService;
    private final AttributeMapper attributeMapper;
    private final AttributeValueMapper attributeValueMapper;

    @GetMapping
    @Operation(summary = "Get attributes")
    public List<AttributeResponse> find() {
        var attributes = this.attributeService.find();

        return this.attributeMapper.toResponseList(attributes);
    }

    @Operation(summary = "Create new attribute with values")
    @PostMapping
    public AttributeResponse create(@Valid @RequestBody AttributeCreateRequest dto) {
        var attribute = this.attributeService.create(dto);
        return this.attributeMapper.toResponse(attribute);
    }

    @PostMapping("/values/{id}")
    @Operation(summary = "Add new values to exsiting attribute.")
    public List<AttributeValueResponse> addValues(@RequestBody List<String> values,
            @PathVariable(name = "id") UUID attributeId) {
        var savedValues = this.attributeService.addValues(values, attributeId);

        return this.attributeValueMapper.toResponseList(savedValues);
    }

    @DeleteMapping("/values")
    @Operation(summary = "Delete values of attribute by value's ids.")
    public ResponseEntity<?> deleteValues(@RequestBody List<UUID> ids) {
        this.attributeService.deleteValuesByAttributeValueIds(ids);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete attibute by id.")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        this.attributeService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
