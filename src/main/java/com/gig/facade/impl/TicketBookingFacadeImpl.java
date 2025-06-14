package com.gig.facade.impl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.dto.CartItemDTO;
import com.gig.dto.OrderDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.OrderIdResponse;
import com.gig.facade.TicketBookingFacade;
import com.gig.models.Cart;
import com.gig.models.Member;
import com.gig.models.Order;
import com.gig.exception.EmptyCartException;
import com.gig.models.*;
import com.gig.service.CartService;
import com.gig.service.OrderService;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
@RequiredArgsConstructor
public class TicketBookingFacadeImpl implements TicketBookingFacade {

    private final CartService cartService;
    private final OrderService orderService;
    private final ApplicationUtilities applicationUtilities;

    @Override
    public CartDTO addTicketToCart(List<CartAddItemDTO> cartAddItemDTO, HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.addTicketToCart(cartAddItemDTO, loggedInMember);
        return new CartDTO(cart);
    }

    @Override
    public String removeTicketFromCart(HttpServletRequest request, UUID ticketId) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.getCartByMember(loggedInMember);
        return cartService.removeTicketFromCart(cart.getId(), ticketId, loggedInMember);
    }

    @Override
    @Transactional
    public CartDTO getCart(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        CartDTO CartDTO = cartService.getCartDTOWithItems(loggedInMember);
        return CartDTO;
    }

    private void mapCartItems(Cart cart) {
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().forEach(item -> {});
        }
    }



    @Override
    public OrderIdResponse checkoutCart(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.getCartByMember(loggedInMember);
        
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException("Cannot checkout: Cart is empty");
        }
        
        Order order = orderService.createOrder(cart, loggedInMember);
        return new OrderIdResponse(order.getId());
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
    
    @Override
    @Transactional
    public CartDTO clearCart(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        Cart cart = cartService.getCartByMember(loggedInMember);
        cartService.clearCart(cart.getId(), loggedInMember);
        return new CartDTO(cart);
    }
}
