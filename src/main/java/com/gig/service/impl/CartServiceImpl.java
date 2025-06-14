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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Cart addTicketToCart(List<CartAddItemDTO> cartAddItemDTOs, Member loggedInMember) {
        Cart cart = getOrCreateCart(loggedInMember);
        
        // First, load all existing cart items for this cart
        Map<UUID, CartItem> existingItems = new HashMap<>();
        for (CartItem existingItem : cart.getCartItems()) {
            existingItems.put(existingItem.getTicket().getId(), existingItem);
        }

        // Process each item in the request
        for (CartAddItemDTO dto : cartAddItemDTOs) {
            Tickets ticket = ticketRepository.findById(dto.getTicketId())
                    .orElseThrow(() -> new RuntimeException("Ticket not found: " + dto.getTicketId()));

            // Check if this ticket already exists in the cart
            CartItem cartItem = existingItems.get(ticket.getId());
            
            if (cartItem == null) {
                // Create new cart item if it doesn't exist
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setTicket(ticket);
                cartItem.setQuantity(dto.getQuantity());
                cartItem.setUnitPrice(ticket.getPrice());
                cartItem.setTotalPrice(dto.getQuantity() * ticket.getPrice());
                cart.addItem(cartItem);
            } else {
                // Update existing cart item
                int newQuantity = cartItem.getQuantity() + dto.getQuantity();
                cartItem.setQuantity(newQuantity);
                cartItem.setTotalPrice(newQuantity * cartItem.getUnitPrice());
            }
            
            // Save the cart item
            cartItemRepository.save(cartItem);
        }
        
        // Calculate and update total amount
        double totalAmount = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalAmount(totalAmount);

        // Save the cart
        return cartRepository.save(cart);
    }


    @Transactional
    public String removeTicketFromCart(UUID cartId, UUID ticketId, Member loggedInMember) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));
            
        CartItem cartItem = cartItemRepository.findByCartIdAndTicketId(cartId, ticketId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Get ticket name for the success message before removing
        String ticketName = cartItem.getTicket() != null ? cartItem.getTicket().getName() : "item";
        
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        
        // Update cart total
        double totalAmount = cart.getCartItems().stream()
            .mapToDouble(item -> item.getTotalPrice())
            .sum();
        cart.setTotalAmount(totalAmount);
        
        cartRepository.save(cart);
        
        return String.format("Successfully removed %s from your cart.", ticketName);
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

            // Clear existing items and add all new ones without replacing the collection
            cart.getCartItems().clear();
            cart.getCartItems().addAll(cartItems);

            // Update the total amount
            double totalAmount = cartItems.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            cart.setTotalAmount(totalAmount);

            // Map to DTO
            return new CartDTO(cart);
        }
        return null;
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

    @Transactional
    public void clearCart(UUID cartId, Member member) {
        // Find the cart with items and verify ownership
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
            
        if (!cart.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("Unauthorized: You do not have permission to clear this cart");
        }
        
        try {
            // First, get all cart item IDs
            List<UUID> itemIds = cart.getCartItems().stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
                
            // Delete all cart items by ID to avoid version conflicts
            if (!itemIds.isEmpty()) {
                cartItemRepository.deleteAllByIdInBatch(itemIds);
            }
            
            // Clear the items collection in memory
            cart.getCartItems().clear();
            
            // Reset cart totals
            cart.setTotalAmount(0.0);
            cart.setTotalItems(0);
            
            // Save the cart
            cartRepository.saveAndFlush(cart);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error clearing cart: " + e.getMessage());
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
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
