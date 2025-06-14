package com.gig.controller;

import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.dto.OrderIdResponse;
import com.gig.facade.TicketBookingFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import com.gig.dto.BaseResponseDto;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class CartController {

    private final TicketBookingFacade ticketBookingFacade;

    @PostMapping("/add-items")
    public ResponseEntity<CartDTO> addToCart(@RequestBody List<CartAddItemDTO> cartAddItemDTO, HttpServletRequest request) {
        CartDTO response = ticketBookingFacade.addTicketToCart(cartAddItemDTO, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove-item/{ticketId}")
    public ResponseEntity<BaseResponseDto> removeFromCart(@PathVariable UUID ticketId, HttpServletRequest request) {
        String message = ticketBookingFacade.removeTicketFromCart(request, ticketId);
        BaseResponseDto response = new BaseResponseDto();
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderIdResponse> checkoutCart(HttpServletRequest request) {
        return ResponseEntity.ok(ticketBookingFacade.checkoutCart(request));
    }

    @GetMapping("/current")
    public ResponseEntity<CartDTO> getCurrentCart(HttpServletRequest request) {
        CartDTO response = ticketBookingFacade.getCart(request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<CartDTO> clearCart(HttpServletRequest request) {
        CartDTO response = ticketBookingFacade.clearCart(request);
        return ResponseEntity.ok(response);
    }
}
