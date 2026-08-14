package com.infinitycart.service;

import com.infinitycart.domain.OrderStatus;
import com.infinitycart.model.Address;
import com.infinitycart.model.Order;
import com.infinitycart.model.User;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address ShippingAddress);
    Order findOrderById(Long id);
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Order cancelOrder(Long orderId, User user);



}
