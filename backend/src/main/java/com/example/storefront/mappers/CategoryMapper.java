package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.storefront.dto.CategoryResponse;
import com.example.storefront.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
