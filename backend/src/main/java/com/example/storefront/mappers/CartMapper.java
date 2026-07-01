package com.example.storefront.mappers;

import org.mapstruct.Mapper;
import com.example.storefront.dto.CartResponse;
import com.example.storefront.entities.Cart;

@Mapper(componentModel = "spring", uses = { CartItemMapper.class })
public interface CartMapper {

    CartResponse toResponse(Cart cart);
}
