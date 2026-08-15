package com.infinitycart.service.impl;

import com.infinitycart.model.Product;
import com.infinitycart.model.User;
import com.infinitycart.model.Wishlist;
import com.infinitycart.repository.WishListRepository;
import com.infinitycart.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    @Override
    public Wishlist createWishList(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlist;
    }

    @Override
    public Wishlist getWishListByUserId(User user) {
       Wishlist wishlist =  wishListRepository.findByUserId(user.getId());
       if(wishlist == null){
           wishlist = createWishList(user);
       }
       return wishlist;
    }

    @Override
    public Wishlist addProductToWishList(User user, Product product) {
        Wishlist wishlist = getWishListByUserId(user);

        if(wishlist.getProducts().contains(product)){
            wishlist.getProducts().remove(product);
        }else{
            wishlist.getProducts().add(product);
        }

        return wishlist;
    }
}
