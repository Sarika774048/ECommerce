package com.infinitycart.repository;

import com.infinitycart.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByUserId(Long userId);
}
