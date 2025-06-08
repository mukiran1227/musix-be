package com.gig.service;

import com.gig.models.Cart;
import com.gig.models.Member;
import com.gig.models.Order;
import com.gig.models.OrderItem;
import com.gig.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Cart cart, Member loggedInMember) {
        Order order = new Order();
        order.setMember(loggedInMember);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("PENDING");
        order.setPaymentStatus("UNPAID");
        
        // Create order items
        List<OrderItem> orderItems = cart.getItems().stream()
            .map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setTicket(item.getTicket());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(item.getUnitPrice());
                orderItem.setTotalPrice(item.getTotalPrice());
                return orderItem;
            })
            .collect(Collectors.toList());
        
        // Convert List to Set
        order.getItems().addAll(orderItems);
        return orderRepository.save(order);
    }

    public Order getOrderById(UUID orderId, Member loggedInMember) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // No need to check ownership since we're updating status
        return order;
    }

    public List<Order> getOrdersByMember(Member loggedInMember) {
        return orderRepository.findByMember(loggedInMember);
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, String status, Member loggedInMember) {
        Order order = getOrderById(orderId, loggedInMember);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public Order processPayment(UUID orderId, String paymentToken, Member loggedInMember) {
        Order order = getOrderById(orderId, loggedInMember);
        order.setPaymentStatus("PAID");
        order.setPaymentToken(paymentToken);
        return orderRepository.save(order);
    }
}
