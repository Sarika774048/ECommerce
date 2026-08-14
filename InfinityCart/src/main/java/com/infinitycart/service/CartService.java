package com.infinitycart.service;


import com.infinitycart.model.Cart;
import com.infinitycart.model.CartItem;
import com.infinitycart.model.Product;
import com.infinitycart.model.User;

public interface CartService {
    CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
            );

    Cart findUserCart(User user);


}
