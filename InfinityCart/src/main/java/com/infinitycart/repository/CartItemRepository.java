package com.infinitycart.repository;

import com.infinitycart.model.Cart;
import com.infinitycart.model.CartItem;
import com.infinitycart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
     CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
