package com.gig.controller;

import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.facade.TicketBookingFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class CartController {

    private final TicketBookingFacade ticketBookingFacade;

    @PostMapping
    public ResponseEntity<CartDTO> addTicketToCart(@RequestBody List<CartAddItemDTO> cartAddItemDTO, HttpServletRequest request) {
        CartDTO response = ticketBookingFacade.addTicketToCart(cartAddItemDTO, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}/ticket/{ticketId}")
    public ResponseEntity<Void> removeTicketFromCart(@PathVariable UUID cartId, @PathVariable UUID ticketId, HttpServletRequest request) {
        ticketBookingFacade.removeTicketFromCart(cartId, ticketId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkout")
    public ResponseEntity<Void> checkoutCart(HttpServletRequest request) {
        ticketBookingFacade.checkoutCart(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(HttpServletRequest request) {
        CartDTO response = ticketBookingFacade.getCart(request);
        return ResponseEntity.ok(response);
    }
}
