package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.storefront.dto.ProductVariantCreateRequest;
import com.example.storefront.dto.ProductVariantResponse;
import com.example.storefront.dto.ProductVariantUpdateRequest;
import com.example.storefront.entities.ProductVariant;

@Mapper(componentModel = "spring", uses = { AssignedAttributeMapper.class })
public interface ProductVariantMapper {

    @Mapping(target = "attributes", source = "assignedAttributes", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    ProductVariantResponse toResponse(ProductVariant variant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "assignedAttributes", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    ProductVariant fromCreateRequest(ProductVariantCreateRequest variant);

    @Mapping(target = "attributes", source = "assignedAttributes", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    ProductVariantCreateRequest toCreateRequest(ProductVariant variant);

    List<ProductVariantCreateRequest> toCreateRequestList(List<ProductVariant> variants);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "assignedAttributes", ignore = true)
    ProductVariant fromUpdateRequest(ProductVariantUpdateRequest variantDto,
            @MappingTarget ProductVariant productVariant);

    @Mapping(target = "attributes", source = "assignedAttributes", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    ProductVariantUpdateRequest toUpdateRequest(ProductVariant variant);
}
