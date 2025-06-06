package com.gig.facade;

import com.gig.dto.EventDTO;
import com.gig.dto.EventSearchDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EventFacade {
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO getEventById(String id);
    List<EventDTO> getAllEvents();
    List<EventDTO> searchEvents(EventSearchDTO searchDTO);
    EventDTO updateEvent(String id, EventDTO updatedEventDTO);
    void deleteEvent(String id);
    List<TicketDTO> getTicketsForEvent(String eventId);
    List<PerformerDTO> getPerformersForEvent(String eventId);
}
