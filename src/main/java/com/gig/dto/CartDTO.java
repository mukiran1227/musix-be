package com.gig.dto;

import com.gig.models.Cart;
import com.gig.models.CartItem;
import com.gig.models.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartDTO {
    private UUID id;
    private double totalAmount;
    private List<CartItem> items;
    private boolean isActive;
    private Member member;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.totalAmount = cart.getTotalAmount();
        this.items = cart.getItems() != null ? 
            cart.getItems().stream().collect(Collectors.toList()) : 
            null;
        this.isActive = cart.isActive();
        this.member = cart.getMember();
    }

    public Cart toEntity() {
        Cart cart = new Cart();
        cart.setId(this.id);
        cart.setTotalAmount(this.totalAmount);
        if (this.items != null) {
            cart.getItems().addAll(this.items);
        }
        cart.setActive(this.isActive);
        cart.setMember(this.member);
        return cart;
    }
}
