package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.storefront.dto.CategoryResponse;
import com.example.storefront.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    CategoryResponse fromEntityToResponse(Category category);

    List<CategoryResponse> fromEntityListToResponseList(List<Category> categories);
}
