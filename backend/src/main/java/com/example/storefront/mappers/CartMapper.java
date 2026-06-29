package com.example.storefront.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.storefront.dto.CartResponse;
import com.example.storefront.entities.AssignedVariantAttribute;
import com.example.storefront.entities.Cart;
import com.example.storefront.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponse fromEtityToResponse(Cart cart);

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
    CartResponse.CartItemResponse fromCartItemToItemResponse(CartItem item);

    List<CartResponse.CartItemResponse> fromCartItemListToItemResponse(List<CartItem> item);

    @Mapping(target = "attributeName", source = "attribute.name")
    @Mapping(target = "valueName", source = "value.name")
    CartResponse.AttributeResponse fromAssignedAttributesToResponse(AssignedVariantAttribute assignedVariantAttribute);

    List<CartResponse.AttributeResponse> fromAssignedAttributeListToResponse(
            List<AssignedVariantAttribute> assignedAttributes);
}
