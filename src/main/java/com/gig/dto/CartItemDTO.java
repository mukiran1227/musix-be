package com.gig.dto;

import com.gig.models.CartItem;
import com.gig.models.Tickets;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemDTO {
    private UUID id;
    private Tickets ticket;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    
    public CartItemDTO() {}
    
    public CartItemDTO(com.gig.models.CartItem cartItem) {
        this.id = cartItem.getId();
        this.ticket = cartItem.getTicket();
        this.quantity = cartItem.getQuantity();
        this.unitPrice = cartItem.getUnitPrice();
        this.totalPrice = cartItem.getTotalPrice();
    }

    public static CartItemDTO fromEntity(CartItem cartItem) {
        if (cartItem == null) return null;
        CartItemDTO dto = new CartItemDTO();
        dto.id = cartItem.getId();
        dto.ticket = cartItem.getTicket();
        dto.quantity = cartItem.getQuantity();
        dto.unitPrice = cartItem.getUnitPrice();
        dto.totalPrice = cartItem.getTotalPrice();
        return dto;
    }

    public CartItem toEntity() {
        CartItem cartItem = new CartItem();
        cartItem.setId(this.id);
        cartItem.setTicket(this.ticket);
        cartItem.setQuantity(this.quantity);
        cartItem.setUnitPrice(this.unitPrice);
        cartItem.setTotalPrice(this.totalPrice);
        return cartItem;
    }
}
