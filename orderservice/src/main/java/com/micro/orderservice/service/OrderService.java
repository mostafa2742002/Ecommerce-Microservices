package com.micro.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.micro.orderservice.entity.Cart;
import com.micro.orderservice.entity.Order;
import com.micro.orderservice.entity.User;
import com.micro.orderservice.repo.CartRepository;
import com.micro.orderservice.repo.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private CartRepository cartRepository;

    public Order placeOrder(Integer cartId, Integer userId) {
        // Retrieve the cart and user to create the order
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        User user = cart.getUser();

        Order order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalPrice(cart.getTotalPrice());

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
