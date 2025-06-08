package com.gig.repository;

import com.gig.models.Cart;
import com.gig.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findByMemberId(UUID memberId);
    Optional<Cart> findByMemberAndIsActive(Member member, boolean isActive);
    Optional<Cart> findById(UUID id);
    Cart findByIdAndMember(UUID cartId, Member member);
    Optional<Cart> findByMember(Member member);
}
