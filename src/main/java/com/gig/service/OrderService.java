package com.gig.service;

import com.gig.models.Cart;
import com.gig.models.Member;
import com.gig.models.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Cart cart, Member member);
    Order getOrderById(UUID orderId, Member member);
    List<Order> getOrdersByMember(Member member);
    Order updateOrderStatus(UUID orderId, String status, Member member);
    Order processPayment(UUID orderId, String paymentToken, Member member);
}
