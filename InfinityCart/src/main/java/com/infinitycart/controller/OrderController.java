package com.infinitycart.controller;

import com.infinitycart.model.*;
import com.infinitycart.service.OrderService;
import com.infinitycart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;


    @PostMapping()
    public ResponseEntity<Set<Order>> createOrder(
            @RequestBody Address shippingAddress,
            @RequestBody Cart cart,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        // Get logged-in user from JWT
        User user = userService.findUserByJwtToken(jwt);

        // Create order(s)
        Set<Order> orders = orderService.createOrder(
                user,
                shippingAddress,
                cart
        );

        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId, @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        OrderItem orderItem = orderService.findById(orderItemId);

        return new ResponseEntity<>(
                orderItem,
                HttpStatus.OK
        );
    }

    @PutMapping("/{orderItemId}/cancel")
    public ResponseEntity<OrderItem> cancelOrderItem(
            @PathVariable Long orderItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        OrderItem orderItem = orderService.findById(orderItemId);

        // Check that the order item belongs to the logged-in user
        if (!user.getId().equals(orderItem.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get the order associated with this order item
        Order order = orderItem.getOrder();

        // Cancel the order
        orderService.cancelOrder(order.getId(), user);

        return new ResponseEntity<>(
                orderItem,
                HttpStatus.OK
        );
    }
}