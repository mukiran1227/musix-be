package com.gig.repository;

import com.gig.models.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Tickets, UUID> {
//    List<Tickets> findByEventId(UUID eventId);
}
