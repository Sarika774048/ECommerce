package com.infinitycart.controller;


import com.infinitycart.domain.OrderStatus;
import com.infinitycart.model.Order;
import com.infinitycart.model.Seller;
import com.infinitycart.service.OrderService;
import com.infinitycart.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
       Seller seller = sellerService.getSellerProfile(jwt);
      List<Order> orders = orderService.sellersOrder(seller.getId());
       return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus

    ) throws Exception {
        Order orders = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

}
