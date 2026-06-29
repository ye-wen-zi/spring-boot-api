package com.example.storefront.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.storefront.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("""
            SELECT DISTINCT c FROM Cart c
                LEFT JOIN FETCH c.items i
                LEFT JOIN FETCH i.productVariant v
                LEFT JOIN FETCH v.product p
                WHERE c.user.id = :userId
            """)
    public Optional<Cart> findByUserIdWithItems(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.id IN :itemIds")
    public void deleteByCartIdAndItemIds(@Param("cartId") Long cartId, @Param("itemIds") Set<Long> itemIds);

    public Optional<Cart> findByUserId(UUID userId);
}
