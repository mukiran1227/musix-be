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
    
    public OrderItemDTO() {}
    
    public OrderItemDTO(com.gig.models.OrderItem orderItem) {
        this.id = orderItem.getId();
        this.ticket = orderItem.getTicket() != null ? new TicketDTO(orderItem.getTicket().getId()) : null;
        this.quantity = orderItem.getQuantity();
        this.unitPrice = orderItem.getUnitPrice();
        this.totalPrice = orderItem.getTotalPrice();
        this.ticketNumber = orderItem.getTicketNumber();
        this.seatNumber = orderItem.getSeatNumber();
        this.section = orderItem.getSection();
    }
}
