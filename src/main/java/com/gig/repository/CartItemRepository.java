package com.gig.repository;

import com.gig.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCartId(UUID cartId);
    Optional<CartItem> findByCartIdAndTicketId(UUID cartId, UUID ticketId);

    @Query(value = "SELECT * FROM cart_items WHERE cart_id = :cartId", nativeQuery = true)
    List<CartItem> findAllByCartIdNative(@Param("cartId") String cartId);

}
