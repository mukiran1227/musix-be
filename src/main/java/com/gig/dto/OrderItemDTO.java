package com.gig.dto;

import com.gig.models.Tickets;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemDTO {
    private UUID id;
    private TicketDTO ticket;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String ticketNumber;
    private String seatNumber;
    private String section;

    public OrderItemDTO(com.gig.models.OrderItem orderItem) {
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.unitPrice = orderItem.getUnitPrice();
        this.totalPrice = orderItem.getTotalPrice();
        this.ticketNumber = orderItem.getTicketNumber();
        this.seatNumber = orderItem.getSeatNumber();
        this.section = orderItem.getSection();
        
        // Set ticket details if available
        if (orderItem.getTicket() != null) {
            Tickets ticket = orderItem.getTicket();
            this.ticket = new TicketDTO(ticket);
        }
    }
}
