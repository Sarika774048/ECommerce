package com.infinitycart.service.impl;

import com.infinitycart.domain.OrderStatus;
import com.infinitycart.model.Address;
import com.infinitycart.model.Order;
import com.infinitycart.model.User;
import com.infinitycart.repository.OrderRepository;
import com.infinitycart.service.AddressRepository;
import com.infinitycart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Override
    public Set<Order> createOrder(User user, Address ShippingAddress) {

        return Set.of();
    }

    @Override
    public Order findOrderById(Long id) {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return List.of();
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return List.of();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
        return null;
    }
}
