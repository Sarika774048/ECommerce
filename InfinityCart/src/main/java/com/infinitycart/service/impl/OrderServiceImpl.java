package com.infinitycart.service.impl;

import com.infinitycart.domain.OrderStatus;
import com.infinitycart.domain.PaymentStatus;
import com.infinitycart.model.*;
import com.infinitycart.repository.OrderItemRepository;
import com.infinitycart.repository.OrderRepository;
import com.infinitycart.service.AddressRepository;
import com.infinitycart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        // Add shipping address to user's addresses if not already present
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }

        // Save shipping address
        Address address = addressRepository.save(shippingAddress);

        // Group cart items by seller
        Map<Long, List<CartItem>> itemsBySeller = cart.getItems()
                .stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getSeller().getId()
                ));

        Set<Order> orders = new HashSet<>();

        // Create one order for each seller
        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {

            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            // Calculate total MRP price
            int totalMrpPrice = items.stream()
                    .mapToInt(item ->
                            item.getMrpPrice() * item.getQuantity()
                    )
                    .sum();

            // Calculate total selling price
            int totalSellingPrice = items.stream()
                    .mapToInt(item ->
                            item.getSellingPrice() * item.getQuantity()
                    )
                    .sum();

            // Calculate total quantity of items
            int totalItem = items.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            // Create order
            Order createdOrder = new Order();

            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrpPrice);
            createdOrder.setTotalSellingPrice(totalSellingPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);

            // Set order status
            createdOrder.setOrderStatus(OrderStatus.PENDING);

            // Set payment status
            createdOrder.getPaymentDetails()
                    .setStatus(PaymentStatus.PENDING);

            // Save order
            Order savedOrder = orderRepository.save(createdOrder);

            // Create order items
            for (CartItem item : items) {

                OrderItem orderItem = new OrderItem();

                orderItem.setOrder(savedOrder);
                orderItem.setProduct(item.getProduct());
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setSellingPrice(item.getSellingPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());

                // Save order item
                OrderItem savedOrderItem =
                        orderItemRepository.save(orderItem);

                // Add order item to order
                savedOrder.getOrderItems().add(savedOrderItem);
            }

            // Add completed order to result
            orders.add(savedOrder);
        }

        return orders;
    }


    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow( () ->  new Exception("Order not found"));

    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId())) {
            throw new Exception("You don't have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findById(Long orderItemId) throws Exception {

        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() ->
                        new Exception("Order item not found"));
    }
}
