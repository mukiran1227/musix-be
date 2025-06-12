package com.gig.dto;

import com.gig.dto.CartItemDTO;
import com.gig.dto.MemberDto;
import com.gig.models.Cart;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartDTO {
    private UUID id;
    private double totalAmount;
    private List<CartItemDTO> items;
    private boolean isActive;


    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.totalAmount = cart.getTotalAmount();
        this.items = cart.getCartItems() != null ?
            cart.getCartItems().stream()
                .map(CartItemDTO::fromEntity)
                .filter(item -> item != null)
                .collect(Collectors.toList()) :
            null;
        this.isActive = cart.isActive();
    }
}
