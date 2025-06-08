package com.gig.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "orders")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();

    private double totalAmount;
    private String status;
    private String paymentStatus;
    private String paymentToken;

    public Order() {
        this.status = "PENDING";
        this.paymentStatus = "UNPAID";
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public double calculateTotalAmount() {
        return items.stream()
            .mapToDouble(OrderItem::getTotalPrice)
            .sum();
    }
}
