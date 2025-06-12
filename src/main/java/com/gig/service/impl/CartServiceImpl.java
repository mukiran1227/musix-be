package com.gig.service.impl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.models.Cart;
import com.gig.models.CartItem;
import com.gig.models.Member;
import com.gig.models.Tickets;
import com.gig.repository.CartItemRepository;
import com.gig.repository.CartRepository;
import com.gig.repository.TicketRepository;
import com.gig.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final TicketRepository ticketRepository;
    private final ApplicationUtilities applicationUtilities;

    /*@Transactional
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
        double totalAmount = cart.getCartItems().stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
        cart.setTotalAmount(totalAmount);
        
        return cartRepository.save(cart);
    }*/

    @Transactional
    public Cart addTicketToCart(List<CartAddItemDTO> cartAddItemDTOs,Member loggedInMember) {
        Cart cart = getOrCreateCart(loggedInMember);

        for (CartAddItemDTO dto : cartAddItemDTOs) {
            Tickets ticket = ticketRepository.findById(dto.getTicketId())
                    .orElseThrow(() -> new RuntimeException("Ticket not found: " + dto.getTicketId()));

            // Find existing cart item
            CartItem cartItem = cartItemRepository.findByCartIdAndTicketId(cart.getId(), dto.getTicketId())
                    .orElseGet(() -> {
                        CartItem newItem = new CartItem();
                        newItem.setCart(cart);
                        newItem.setTicket(ticket);
                        newItem.setUnitPrice(ticket.getPrice());
                        newItem.setQuantity(0); // Start with 0 and add below
                        return newItem;
                    });

            // Update quantity and total price
            int newQuantity = cartItem.getQuantity() + dto.getQuantity();
            cartItem.setQuantity(newQuantity);
            cartItem.setTotalPrice(newQuantity * cartItem.getUnitPrice());

            // Save cart item
            cartItemRepository.save(cartItem);
        }

        List<CartItem> cartItems = cartItemRepository.findAllByCartIdNative(cart.getId().toString());

        double totalAmount = emptyIfNull(cartItems).stream()
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

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        
        // Update cart total
        double totalAmount = cart.getCartItems().stream()
            .mapToDouble(item -> item.getTotalPrice())
            .sum();
        cart.setTotalAmount(totalAmount);
        
        cartRepository.save(cart);
    }

    @Transactional
    public Cart getCartByMember(Member loggedInMember) {
        return cartRepository.findByMember(loggedInMember.getId().toString())
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setMember(loggedInMember);
                return cartRepository.save(cart);
            });
    }

    @Transactional
    public CartDTO getCartDTOWithItems(Member member) {
        Cart cart = cartRepository.findCartWithItems(member.getId().toString());

        if (ObjectUtils.isNotEmpty(cart)) {
            List<CartItem> cartItems = cartItemRepository.findAllByCartIdNative(cart.getId().toString());

            // Manually set the cartItems into the cart entity
            cart.setCartItems(new HashSet<>(cartItems));

            // Optionally update the total if needed
            double totalAmount = cartItems.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            cart.setTotalAmount(totalAmount);

            // Map to DTO
            return new CartDTO(cart);
        }

        return null; // or throw exception, or return empty DTO
    }




    @Transactional
    private Cart getOrCreateCart(Member member) {
        return cartRepository.findByMemberAndIsActive(member.getId().toString(), true)
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
