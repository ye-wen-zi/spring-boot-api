package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.storefront.dto.AttributeResponse;
import com.example.storefront.entities.Attribute;

@Mapper(componentModel = "spring", uses = { AttributeValueMapper.class })
public interface AttributeMapper {

    AttributeResponse toResponse(Attribute attribute);

    List<AttributeResponse> toResponseList(List<Attribute> attribute);
}
