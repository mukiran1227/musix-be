package com.gig.service;

import com.gig.dto.CartAddItemDTO;
import com.gig.dto.CartDTO;
import com.gig.models.Cart;
import com.gig.models.CartItem;
import com.gig.models.Member;
import com.gig.models.Tickets;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    Cart addTicketToCart(List<CartAddItemDTO> cartAddItemDTOs, Member member);
    void removeTicketFromCart(UUID cartId, UUID ticketId, Member member);
    Cart getCartByMember(Member loggedInMember);
    void checkoutCart(UUID cartId);
    CartItem createOrUpdateCartItem(Cart cart, Tickets ticket, int quantity);

    CartDTO getCartDTOWithItems(Member loggedInMember);
}
