package com.infinitycart.service;

import com.infinitycart.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long id, CartItem cartitem);

    void removeCartItem(Long userId, Long cartItemId);
    CartItem findCartItemById(Long id);
}
