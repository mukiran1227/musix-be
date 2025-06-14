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
@Entity(name = "carts")
@EqualsAndHashCode(callSuper = true)
public class Cart extends BaseEntity {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private double totalAmount;
    private boolean isActive = true;

    public Cart() {
        this.isActive = true;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void addItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
        item.setCart(null);
    }
    
    public void setTotalItems(int count) {
        // This is a derived value, but we'll keep it for backward compatibility
        // The actual count is always derived from cartItems.size()
    }

    public double calculateTotalAmount() {
        return cartItems.stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
    }
}
