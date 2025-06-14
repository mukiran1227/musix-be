package com.gig.repository;

import com.gig.models.Cart;
import com.gig.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query(value = "SELECT c.* FROM carts c LEFT JOIN cart_items ci ON c.id = ci.cart_id WHERE c.member_id = :memberId AND c.is_active = :isActive", nativeQuery = true)
    Optional<Cart> findByMemberAndIsActive(@Param("memberId") String memberId, @Param("isActive") boolean isActive);

    @Query(value = "SELECT c.* FROM carts c LEFT JOIN cart_items ci ON c.id = ci.cart_id WHERE c.id = :id", nativeQuery = true)
    Optional<Cart> findById(@Param("id") String id);

    @Query(value = "SELECT c.* FROM carts c  WHERE c.member_id = :memberId", nativeQuery = true)
    Optional<Cart> findByMember(@Param("memberId") String memberId);

    @Query(value = "SELECT c.* FROM carts c " +
            "LEFT JOIN cart_items ci ON c.id = ci.cart_id " +
            "WHERE c.member_id = :memberId", nativeQuery = true)
    Cart findCartWithItems(@Param("memberId") String memberId);
    
    @Query(value = "SELECT c.* FROM carts c " +
            "LEFT JOIN cart_items ci ON c.id = ci.cart_id " +
            "WHERE c.id = :cartId", nativeQuery = true)
    Optional<Cart> findByIdWithItems(@Param("cartId") UUID cartId);

}
