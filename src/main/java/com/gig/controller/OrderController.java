package com.gig.controller;

import com.gig.dto.CartDTO;
import com.gig.dto.OrderDTO;
import com.gig.dto.TicketDTO;
import com.gig.facade.TicketBookingFacade;
import com.gig.models.Cart;
import com.gig.models.Member;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class OrderController {

    private final TicketBookingFacade ticketBookingFacade;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CartDTO cartDTO, HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        if (member == null) {
            throw new RuntimeException("Member not found in request");
        }
        
        Cart cart = cartDTO.toEntity();
        cart.setMember(member);
        OrderDTO response = ticketBookingFacade.createOrder(cart, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(HttpServletRequest request) {
        List<OrderDTO> response = ticketBookingFacade.getOrdersByMember(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getCart(HttpServletRequest request) {
        CartDTO response = ticketBookingFacade.getCart(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkoutCart(HttpServletRequest request) {
        OrderDTO response = ticketBookingFacade.checkoutCart(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable UUID orderId,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.getOrderById(orderId, request));
    }

    @PutMapping("/{orderId}/status/{status}")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable UUID orderId,
            @PathVariable String status,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.updateOrderStatus(orderId, status, request));
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<OrderDTO> processPayment(
            @PathVariable UUID orderId,
            @RequestBody String paymentToken,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.processPayment(orderId, paymentToken, request));
    }

    @GetMapping("/event/{eventId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAvailableTickets(
            @PathVariable UUID eventId,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.getAvailableTickets(eventId, request));
    }
}
