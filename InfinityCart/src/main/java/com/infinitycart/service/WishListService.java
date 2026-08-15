package com.infinitycart.service;

import com.infinitycart.model.Product;
import com.infinitycart.model.User;
import com.infinitycart.model.Wishlist;

public interface WishListService {
    Wishlist createWishList(User user);
    Wishlist getWishListByUserId(User user);
    Wishlist addProductToWishList(User user, Product product);
}
