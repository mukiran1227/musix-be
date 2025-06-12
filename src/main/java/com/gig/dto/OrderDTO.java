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
    private double totalAmount;
    private String status;
    private String paymentStatus;
    private String qrCodeData;
    private List<OrderItemDTO> items;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.qrCodeData = order.getQrCodeData();
        this.paymentStatus = order.getPaymentStatus();
        this.items = order.getItems() != null
                ? order.getItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList())
                : null;
    }
}
