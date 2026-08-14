package com.infinitycart.service.impl;


import com.infinitycart.model.CartItem;
import com.infinitycart.model.User;
import com.infinitycart.repository.CartItemRepository;
import com.infinitycart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartitem) {
        CartItem item = findCartItemById(id);

        User cartItemUser = item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)){
            item.setQuantity(cartitem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }
        return null;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem item = findCartItemById(cartItemId);

        User cartItemUser = item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)){
            cartItemRepository.delete(item);
        }
    }

    @Override
    public CartItem findCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        return null;
    }
}
