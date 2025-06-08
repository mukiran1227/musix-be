package com.gig.dto;

import com.gig.models.Member;
import com.gig.models.Order;
import com.gig.models.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDTO {
    private UUID id;
    private Member member;
    private double totalAmount;
    private String status;
    private String paymentStatus;
    private List<OrderItem> items;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.member = order.getMember();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.paymentStatus = order.getPaymentStatus();
        this.items = order.getItems() != null ?
                new ArrayList<>(order.getItems()) :
            null;
    }

    public Order toEntity() {
        Order order = new Order();
        order.setId(this.id);
        order.setMember(this.member);
        order.setTotalAmount(this.totalAmount);
        order.setStatus(this.status);
        order.setPaymentStatus(this.paymentStatus);
        if (this.items != null) {
            order.getItems().addAll(this.items);
        }
        return order;
    }
}
