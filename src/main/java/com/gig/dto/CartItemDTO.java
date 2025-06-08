package com.gig.dto;

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
}
