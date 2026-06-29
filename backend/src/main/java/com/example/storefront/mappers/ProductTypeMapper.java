package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.storefront.dto.ProductTypeResponse;
import com.example.storefront.entities.ProductType;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    
    ProductTypeResponse fromEntityToResponse(ProductType productType);

    List<ProductTypeResponse> fromEntityListToResponse(List<ProductType> productType);
}
