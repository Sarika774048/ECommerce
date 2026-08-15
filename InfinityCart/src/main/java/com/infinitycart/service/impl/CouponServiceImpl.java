package com.infinitycart.service.impl;

import com.infinitycart.model.Cart;
import com.infinitycart.model.Coupon;
import com.infinitycart.model.User;
import com.infinitycart.repository.CartRepository;
import com.infinitycart.repository.CouponRepository;
import com.infinitycart.repository.UserRepository;
import com.infinitycart.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);

        Cart cart = cartRepository.findByUserId(user.getId());

        if(coupon == null){
            throw new Exception("Coupon not Valid");
        }
        if(user.getUsedCoupons().contains(coupon)){
            throw new Exception("Coupon already exists");
        }
        if(orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("Valid for minimum order value" + coupon.getMinimumOrderValue());
        }

        if(coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) && LocalDate.now().isBefore(coupon.getValidityEndDate()) ){
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;

        }
        return null;
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception{
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon == null){
            throw new Exception("Coupon not Valid");
        }

        Cart cart = cartRepository.findByUserId(user.getId());

        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);
       return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long id) {
    findCouponById(id);
    couponRepository.deleteById(id);
    }
}
