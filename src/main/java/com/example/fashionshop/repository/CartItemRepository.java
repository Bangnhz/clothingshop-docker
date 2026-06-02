package com.example.fashionshop.repository;

import com.example.fashionshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndProductVariantId(Integer cartId, Long variantId);

    List<CartItem> findByCartId(Integer id);
}
