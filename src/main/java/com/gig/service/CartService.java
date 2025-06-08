package com.gig.service;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.CartAddItemDTO;
import com.gig.models.Cart;
import com.gig.models.CartItem;
import com.gig.models.Member;
import com.gig.models.Tickets;
import com.gig.repository.CartItemRepository;
import com.gig.repository.CartRepository;
import com.gig.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final TicketRepository ticketRepository;
    private final ApplicationUtilities applicationUtilities;

    @Transactional
    public Cart addTicketToCart(List<CartAddItemDTO> cartAddItemDTOs, Member loggedInMember) {
        Cart cart = getOrCreateCart(loggedInMember);
        
        // Process each item in the list
        for (CartAddItemDTO dto : cartAddItemDTOs) {
            // Get or create the ticket
            Tickets ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + dto.getTicketId()));
            
            // Create or update cart item
            CartItem cartItem = cartItemRepository.findByCartIdAndTicketId(cart.getId(), dto.getTicketId())
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setTicket(ticket);
                    item.setQuantity(dto.getQuantity());
                    item.setUnitPrice(ticket.getPrice());
                    item.setTotalPrice(dto.getQuantity() * ticket.getPrice());
                    return item;
                });

            // Update existing quantity if item exists
            if (cartItem.getId() != null) {
                cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
                cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getUnitPrice());
            }

            // Save the cart item
            cartItemRepository.save(cartItem);
        }
        
        // Update cart total
        double totalAmount = cart.getItems().stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
        cart.setTotalAmount(totalAmount);
        
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeTicketFromCart(UUID cartId, UUID ticketId, Member loggedInMember) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));
            
        CartItem cartItem = cartItemRepository.findByCartIdAndTicketId(cartId, ticketId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        
        // Update cart total
        double totalAmount = cart.getItems().stream()
            .mapToDouble(item -> item.getTotalPrice())
            .sum();
        cart.setTotalAmount(totalAmount);
        
        cartRepository.save(cart);
    }

    @Transactional
    public Cart getCartByMember(Member loggedInMember) {
        return cartRepository.findByMember(loggedInMember)
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setMember(loggedInMember);
                return cartRepository.save(cart);
            });
    }

    private Cart getOrCreateCart(Member member) {
        return cartRepository.findByMemberAndIsActive(member, true)
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setMember(member);
                cart.setActive(true);
                return cartRepository.save(cart);
            });
    }

    @Transactional
    public void checkoutCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));
            
        cart.setActive(false);
        cartRepository.save(cart);
    }

    public CartItem createOrUpdateCartItem(Cart cart, Tickets ticket, int quantity) {
        CartItem cartItem = cartItemRepository.findByCartIdAndTicketId(cart.getId(), ticket.getId())
            .orElseGet(() -> {
                CartItem item = new CartItem();
                item.setCart(cart);
                item.setTicket(ticket);
                item.setQuantity(quantity);
                item.setUnitPrice(ticket.getPrice());
                item.setTotalPrice(quantity * ticket.getPrice());
                return item;
            });

        // Update existing quantity if item exists
        if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getUnitPrice());
        }

        return cartItemRepository.save(cartItem);
    }
}
