package com.example.storefront.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.storefront.dto.CartItemRequest;
import com.example.storefront.entities.Cart;
import com.example.storefront.entities.CartItem;
import com.example.storefront.entities.ProductVariant;
import com.example.storefront.entities.User;
import com.example.storefront.exceptions.ResourceNotFoundException;
import com.example.storefront.repositories.CartRepository;
import com.example.storefront.repositories.ProductVariantRepository;
import com.example.storefront.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;

    public Cart findByUserIdWithItems(UUID id) {
        return this.cartRepository.findByUserIdWithItems(id).orElseGet(() -> this.createNewCart(id));
    }

    @Transactional
    public List<CartItem> addItemsToCart(List<CartItemRequest> dto, UUID userId) {
        var cart = this.findByUserIdWithItems(userId);

        var existingItemsMap = cart.getItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getProductVariant().getId(),
                        item -> item,
                        (existing, replacement) -> existing // Chống lỗi Duplicate Key
                ));

        var variantIds = dto.stream()
                .map(CartItemRequest::variantId)
                .distinct()
                .toList();

        var productVariantsMap = this.productVariantRepository.findAllById(variantIds)
                .stream()
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        for (var itemReq : dto) {
            var existingItem = existingItemsMap.get(itemReq.variantId());

            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + itemReq.quantity();
                if (newQuantity <= 0) {
                    cart.getItems().remove(existingItem);
                } else {
                    existingItem.setQuantity(existingItem.getQuantity() + itemReq.quantity());
                }
            } else {
                var variant = productVariantsMap.get(itemReq.variantId());
                if (variant == null) {
                    throw new ResourceNotFoundException("Product variant not found with id: " + itemReq.variantId());
                }

                CartItem newItem = CartItem.builder()
                        .cart(cart)
                        .productVariant(variant)
                        .quantity(itemReq.quantity())
                        .build();

                cart.getItems().add(newItem);
            }
        }

        var savedCart = this.cartRepository.save(cart);
        var variantIdDtoSet = dto.stream().map(item -> item.variantId()).collect(Collectors.toSet());
        return savedCart.getItems().stream().filter(item -> variantIdDtoSet.contains(item.getProductVariant().getId()))
                .toList();

    }

    public void removeItemsFromCart(Set<Long> itemIds, UUID userId) {
        var cartOption = this.cartRepository.findByUserId(userId);
        if (cartOption.isPresent()) {
            this.cartRepository.deleteByCartIdAndItemIds(cartOption.get().getId(), itemIds);
        }
    }

    private Cart createNewCart(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not exists!"));

        Cart newCart = Cart.builder().user(user).build();
        return cartRepository.save(newCart);
    }
}
