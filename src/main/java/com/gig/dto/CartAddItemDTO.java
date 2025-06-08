package com.gig.dto;

import com.gig.models.Tickets;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartAddItemDTO {
    private UUID ticketId;
    private int quantity;
}
