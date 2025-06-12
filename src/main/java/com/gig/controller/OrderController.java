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

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDTO>> getMyOrders(HttpServletRequest request) {
        List<OrderDTO> response = ticketBookingFacade.getOrdersByMember(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(
            @PathVariable UUID orderId,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.getOrderById(orderId, request));
    }

    @PutMapping("/{orderId}/update-status/{status}")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable UUID orderId,
            @PathVariable String status,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.updateOrderStatus(orderId, status, request));
    }

    @PostMapping("/{orderId}/process-payment")
    public ResponseEntity<OrderDTO> processPayment(
            @PathVariable UUID orderId,
            @RequestBody String paymentToken,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.processPayment(orderId, paymentToken, request));
    }

    @GetMapping("/event/{eventId}/available-tickets")
    public ResponseEntity<List<TicketDTO>> getAvailableTickets(
            @PathVariable UUID eventId,
            HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.getAvailableTickets(eventId, request));
    }
}
