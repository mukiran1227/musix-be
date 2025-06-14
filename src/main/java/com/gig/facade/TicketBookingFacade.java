package com.gig.facade;

import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.dto.OrderDTO;
import com.gig.dto.OrderIdResponse;
import com.gig.dto.TicketDTO;
import com.gig.models.Cart;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface TicketBookingFacade {
    CartDTO addTicketToCart(List<CartAddItemDTO> cartAddItemDTO, HttpServletRequest request);
    void removeTicketFromCart(HttpServletRequest request, UUID ticketId);
    CartDTO getCart(HttpServletRequest request);
    OrderIdResponse checkoutCart(HttpServletRequest request);
    OrderDTO createOrder(Cart cart, HttpServletRequest request);
    List<OrderDTO> getOrdersByMember(HttpServletRequest request);
    OrderDTO getOrderById(UUID orderId, HttpServletRequest request);
    OrderDTO updateOrderStatus(UUID orderId, String status, HttpServletRequest request);
    OrderDTO processPayment(UUID orderId, String paymentToken, HttpServletRequest request);
    List<TicketDTO> getAvailableTickets(UUID eventId, HttpServletRequest request);
    
    /**
     * Clears all items from the user's cart
     * @param request The HTTP request containing the user's authentication token
     * @return The updated cart DTO
     */
    CartDTO clearCart(HttpServletRequest request);
}
