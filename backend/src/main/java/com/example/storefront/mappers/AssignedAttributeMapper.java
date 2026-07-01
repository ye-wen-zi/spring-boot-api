package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.storefront.dto.AssignedAttributeCreateRequest;
import com.example.storefront.dto.AssignedAttributeResponse;
import com.example.storefront.dto.AssignedAttributeUpdateRequest;
import com.example.storefront.entities.AssignedVariantAttribute;

@Mapper(componentModel = "spring")
public interface AssignedAttributeMapper {
    @Mapping(target = "attributeId", source = "attribute.id")
    @Mapping(target = "attributeValueId", source = "value.id")
    AssignedAttributeCreateRequest toCreateRequest(AssignedVariantAttribute attribute);

    @Mapping(target = "attributeName", source = "attribute.name")
    @Mapping(target = "valueName", source = "value.name")
    AssignedAttributeResponse toResponse(AssignedVariantAttribute assignedAttribute);

    List<AssignedAttributeResponse> toResponseList(List<AssignedVariantAttribute> assignedAttributes);

    @Mapping(target = "assignedId", source = "id")
    @Mapping(target = "attributeId", source = "attribute.id")
    @Mapping(target = "attributeValueId", source = "value.id")
    AssignedAttributeUpdateRequest toUpdateRequest(AssignedVariantAttribute attribute);

    // @Named("mapAssignedAttributes")
    // default List<AssignedAttributeResponse> mapAssignedAttributes(
    // List<AssignedVariantAttribute> assignedAttributes) {
    // if (assignedAttributes == null) {
    // return new ArrayList<>();
    // }

    // List<AssignedAttributeResponse> result = new ArrayList<>();

    // for (AssignedVariantAttribute assigned : assignedAttributes) {
    // String attrName = assigned.getAttribute().getName();

    // result.add(new AssignedAttributeResponse(attrName,
    // assigned.getValue().getName()));
    // }
    // return result;
    // }
}
