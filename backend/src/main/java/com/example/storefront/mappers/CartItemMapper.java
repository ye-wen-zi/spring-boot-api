package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.storefront.dto.CartItemResponse;
import com.example.storefront.entities.CartItem;

@Mapper(componentModel = "spring", uses = { AssignedAttributeMapper.class })
public interface CartItemMapper {
    @Mapping(target = "productId", source = "productVariant.product.id")
    @Mapping(target = "variantId", source = "productVariant.id")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "variantName", source = "productVariant.name")
    @Mapping(target = "productSlug", source = "productVariant.product.slug")
    @Mapping(target = "thumbnailUrl", source = "productVariant.product.thumbnail")
    @Mapping(target = "unitPrice", source = "productVariant.price")
    @Mapping(target = "remaining", source = "productVariant.quantity")
    // @Mapping(target = "attributes", source = "productVariant.assignedAttributes",
    // qualifiedByName = "fromAssignedAttributeListToResponse")
    @Mapping(target = "attributes", source = "productVariant.assignedAttributes", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    CartItemResponse toItemResponse(CartItem item);

    List<CartItemResponse> toItemResponseList(List<CartItem> item);
}
