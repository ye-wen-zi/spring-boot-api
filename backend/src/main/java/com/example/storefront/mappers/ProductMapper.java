package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.dto.ProductResponse;
import com.example.storefront.dto.ProductUpdateRequest;
import com.example.storefront.entities.Product;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class, ProductVariantMapper.class,
        AssignedAttributeMapper.class })
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "productTypeName", source = "productType.name")
    @Mapping(target = "variants", source = "variants")
    ProductDetailResponse toDetailResponse(Product product);

    List<ProductDetailResponse> toDetailResponseList(List<Product> products);

    // @Mapping(target = "category", source = "category")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productType", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "minPrice", ignore = true)
    @Mapping(target = "maxPrice", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "rating", ignore = true)
    Product fromCreateRequest(ProductCreateRequest dto);

    List<Product> fromCreateRequestList(List<ProductCreateRequest> dto);

    @Mapping(target = "typeId", source = "productType.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", source = "variants")
    ProductCreateRequest toCreateRequest(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productType", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "minPrice", ignore = true)
    @Mapping(target = "maxPrice", ignore = true)
    @Mapping(target = "variants", ignore = true)
    Product fromUpdateRequest(ProductUpdateRequest dto, @MappingTarget Product product);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "typeId", source = "productType.id")
    @Mapping(target = "images", expression = "java(new java.util.ArrayList())")
    ProductUpdateRequest toUpdateRequest(Product product);

}

// Tránh vòng lặp vô tận: Nếu Product có Category, và Category lại có
// List<Product>, => StackOverflowError khi map => @Mapping(target =
// "products", ignore = true) để ngắt vòng lặp.