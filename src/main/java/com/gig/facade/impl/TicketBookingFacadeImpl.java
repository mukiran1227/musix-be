package com.gig.facade.impl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.dto.OrderDTO;
import com.gig.dto.TicketDTO;
import com.gig.facade.TicketBookingFacade;
import com.gig.models.Cart;
import com.gig.models.Member;
import com.gig.models.Order;
import com.gig.models.Tickets;
import com.gig.repository.TicketRepository;
import com.gig.service.CartService;
import com.gig.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class TicketBookingFacadeImpl implements TicketBookingFacade {

    private final CartService cartService;
    private final OrderService orderService;
    private final TicketRepository ticketRepository;
    private final ApplicationUtilities applicationUtilities;

    @Override
    public CartDTO addTicketToCart(List<CartAddItemDTO> cartAddItemDTO, HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.addTicketToCart(cartAddItemDTO, loggedInMember);
        return new CartDTO(cart);
    }

    @Override
    public void removeTicketFromCart(UUID cartId, UUID ticketId, HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        cartService.removeTicketFromCart(cartId, ticketId,loggedInMember);
    }

    @Override
    public CartDTO getCart(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.getCartByMember(loggedInMember);
        return new CartDTO(cart);
    }

    @Override
    public OrderDTO checkoutCart(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.getCartByMember(loggedInMember);
        Order order = orderService.createOrder(cart, loggedInMember);
        return new OrderDTO(order);
    }

    @Override
    public OrderDTO createOrder(Cart cart, HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Order order = orderService.createOrder(cart, loggedInMember);
        return new OrderDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByMember(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        List<Order> orders = orderService.getOrdersByMember(loggedInMember);
        return orders.stream()
            .map(OrderDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(UUID orderId, HttpServletRequest request) {
        Member member = applicationUtilities.getLoggedInUser(request);
        Order order = orderService.getOrderById(orderId, member);
        return new OrderDTO(order);
    }

    @Override
    public OrderDTO updateOrderStatus(UUID orderId, String status, HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Order order = orderService.updateOrderStatus(orderId, status, loggedInMember);
        return new OrderDTO(order);
    }

    @Override
    public OrderDTO processPayment(UUID orderId, String paymentToken, HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Order order = orderService.processPayment(orderId, paymentToken, loggedInMember);
        return new OrderDTO(order);
    }

    @Override
    public List<TicketDTO> getAvailableTickets(UUID eventId, HttpServletRequest request) {
        /*Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        // Get all tickets for the event
        List<Tickets> tickets = ticketRepository.findByEventId(eventId);

        // Convert to DTOs
        return tickets.stream()
            .map(ticket -> {
                TicketDTO dto = new TicketDTO();
                dto.setId(ticket.getId());
                dto.setName(ticket.getName());
                dto.setDescription(ticket.getDescription());
                dto.setPrice(ticket.getPrice());
                return dto;
            })
            .collect(Collectors.toList());*/
        return null;
    }
}
