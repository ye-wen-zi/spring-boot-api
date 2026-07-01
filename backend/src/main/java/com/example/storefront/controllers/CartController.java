package com.example.storefront.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.dto.CartItemCreateRequest;
import com.example.storefront.dto.CartItemResponse;
import com.example.storefront.dto.CartResponse;
import com.example.storefront.entities.Cart;
import com.example.storefront.entities.CustomUserDetails;
import com.example.storefront.mappers.CartItemMapper;
import com.example.storefront.mappers.CartMapper;
import com.example.storefront.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "APIs related to cart management.")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @GetMapping
    @Operation(summary = "Get cart")
    public CartResponse getItems(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Cart cart = this.cartService.findByUserIdWithItems(userDetails.getId());
        return this.cartMapper.toResponse(cart);
    }

    @PostMapping("/items")
    @Operation(summary = "Add / Increase / Decrease item quantity in the cart")
    public List<CartItemResponse> addItemsToCart(@RequestBody List<CartItemCreateRequest> dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        var cartItems = this.cartService.addItemsToCart(dto, userDetails.getId());
        return this.cartItemMapper.toItemResponseList(cartItems);
    }

    @DeleteMapping("/items")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<?> removeItems(@RequestBody Set<Long> itemIds,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        this.cartService.removeItemsFromCart(itemIds, customUserDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
