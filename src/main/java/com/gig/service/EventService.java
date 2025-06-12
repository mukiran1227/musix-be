package com.gig.service;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.models.Member;

import java.util.List;

public interface EventService {
    BaseResponseDto createEvent(EventDTO eventDTO, Member loggedInMember);
    EventDTO getEventById(String id);
    List<SimpleEventDTO> getAllEvents(int page, int size);
    long countAllEvents();
    List<SimpleEventDTO> getUserEvents(int page, int size, Member loggedInMember);
    long countUserEvents(Member loggedInMember);
    BaseResponseDto updateEvent(String id, EventDTO updatedEventDTO, Member loggedInMember);
    void deleteEvent(String id, Member loggedInMember);

    List<SimpleEventDTO> getEventsByCategory(int page, int size, String category, String eventId);
    long countEventsByCategory(String category);
    List<TicketDTO> getTicketsForEvent(String eventId, Member loggedInMember);
    List<PerformerDTO> getPerformersForEvent(String eventId);
}
