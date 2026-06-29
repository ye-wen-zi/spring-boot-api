package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.storefront.dto.AttributeResponse;
import com.example.storefront.entities.Attribute;
import com.example.storefront.entities.AttributeValue;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    AttributeResponse fromEntityToResponse(Attribute attribute);

    List<AttributeResponse> fromEntityListToResponse(List<Attribute> attribute);

    AttributeResponse.AttributeValueResponse fromEntityValueToResponse(AttributeValue values);

    List<AttributeResponse.AttributeValueResponse> fromEntityValueListToResponse(List<AttributeValue> values);
}
