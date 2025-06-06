package com.gig.service;

import com.gig.dto.EventDTO;
import com.gig.dto.EventSearchDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;

import java.util.List;

public interface EventService {
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO getEventById(String id);
    List<EventDTO> getAllEvents();
    List<EventDTO> searchEvents(EventSearchDTO searchDTO);
    EventDTO updateEvent(String id, EventDTO updatedEventDTO);
    void deleteEvent(String id);
    List<TicketDTO> getTicketsForEvent(String eventId);
    List<PerformerDTO> getPerformersForEvent(String eventId);
}
