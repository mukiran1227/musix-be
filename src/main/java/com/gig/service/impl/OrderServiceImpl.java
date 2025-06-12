package com.gig.service.impl;

import com.gig.models.Cart;
import com.gig.models.CartItem;
import com.gig.models.Member;
import com.gig.models.Order;
import com.gig.models.OrderItem;
import com.gig.repository.CartItemRepository;
import com.gig.repository.CartRepository;
import com.gig.repository.OrderItemRepository;
import com.gig.repository.OrderRepository;
import com.gig.service.OrderService;
import com.gig.util.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    /*@Transactional
    public Order createOrder(Cart cart, Member loggedInMember) {
        Order order = new Order();
        order.setMember(loggedInMember);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("PENDING");
        order.setPaymentStatus("UNPAID");
        
        // Create order items
        Set<OrderItem> orderItems = cart.getCartItems().stream()
            .map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setTicket(item.getTicket());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(item.getUnitPrice());
                orderItem.setTotalPrice(item.getTotalPrice());
                orderItemRepository.save(orderItem);
                return orderItem;
            })
            .collect(Collectors.toSet());
        order.setItems(orderItems);
        // Convert List to Set
        return orderRepository.save(order);
    }
*/

    @Transactional
    public Order createOrder(Cart cart, Member loggedInMember) {
        // Load cart with items to ensure they're in the persistence context
        Cart managedCart = cartRepository.findById(cart.getId().toString())
            .orElseThrow(() -> new RuntimeException("Cart not found"));
            
        // Get cart items from the managed cart
        List<CartItem> cartItems = cartItemRepository.findAllByCartIdNative(managedCart.getId().toString());
        
        // Create new order
        Order order = new Order();
        order.setMember(loggedInMember);
        order.setTotalAmount(managedCart.getTotalAmount());
        order.setStatus("PENDING");
        order.setPaymentStatus("UNPAID");

        // Create and add order items
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setTicket(cartItem.getTicket());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            order.addItem(orderItem);
        }

        // Save the order first
        Order savedOrder = orderRepository.save(order);
        
        try {
            // Generate QR code data
            String qrCodeData = generateOrderQRCodeData(savedOrder);
            String qrCodeBase64 = QRCodeGenerator.generateQRCode(qrCodeData);
            
            // Save QR code to order
            savedOrder.setQrCodeData(qrCodeBase64);
            savedOrder = orderRepository.save(savedOrder);
        } catch (Exception e) {
            logger.error("Failed to generate QR code for order: " + savedOrder.getId(), e);
            // Continue without QR code if generation fails
        }
        
        // Clear cart items and update cart
        if (!cartItems.isEmpty()) {
            // Clear the relationship first
            for (CartItem item : new ArrayList<>(cartItems)) {
                managedCart.removeItem(item);
            }
            // Delete all cart items
            cartItemRepository.deleteAll(cartItems);
            // Reset cart total amount to zero
            managedCart.setTotalAmount(0.0);
            // Update the cart to reflect the changes
            cartRepository.save(managedCart);
        }
        
        return savedOrder;
    }
    
    private String generateOrderQRCodeData(Order order) {
        return String.format(
            "Order ID: %s\n" +
            "Customer: %s\n" +
            "Total Amount: %.2f\n" +
            "Status: %s\n" +
            "Items: %d",
            order.getId(),
            order.getMember().getUsername(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getItems().size()
        );
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
