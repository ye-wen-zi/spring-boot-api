package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.storefront.dto.AttributeValueResponse;
import com.example.storefront.entities.AttributeValue;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    
    AttributeValueResponse toResponse(AttributeValue values);

    List<AttributeValueResponse> toResponseList(List<AttributeValue> values);
}
