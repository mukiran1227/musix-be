package com.gig.facade.impl;

import com.gig.dto.EventDTO;
import com.gig.dto.EventSearchDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.TicketDTO;
import com.gig.facade.EventFacade;
import com.gig.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class EventFacadeImpl implements EventFacade {

    @Autowired
    private EventService eventService;

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }

    @Override
    public EventDTO getEventById(String id) {
        return eventService.getEventById(id);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Override
    public List<EventDTO> searchEvents(EventSearchDTO searchDTO) {
        return eventService.searchEvents(searchDTO);
    }

    @Override
    public EventDTO updateEvent(String id, EventDTO updatedEventDTO) {
        return eventService.updateEvent(id, updatedEventDTO);
    }

    @Override
    public void deleteEvent(String id) {
        eventService.deleteEvent(id);
    }

    @Override
    public List<TicketDTO> getTicketsForEvent(String eventId) {
        return eventService.getTicketsForEvent(eventId);
    }

    @Override
    public List<PerformerDTO> getPerformersForEvent(String eventId) {
        return eventService.getPerformersForEvent(eventId);
    }
}
