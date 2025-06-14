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
    EventDTO getEventById(String id, Member loggedInMember);
    List<SimpleEventDTO> getAllEvents(int page, int size, Member loggedInMember);
    long countAllEvents();
    List<SimpleEventDTO> getUserEvents(int page, int size, Member loggedInMember);
    long countUserEvents(Member loggedInMember);
    BaseResponseDto updateEvent(String id, EventDTO updatedEventDTO, Member loggedInMember);
    void deleteEvent(String id, Member loggedInMember);

    List<SimpleEventDTO> getEventsByCategory(int page, int size, String category, String eventId, Member loggedInMember);
    long countEventsByCategory(String category, String eventId);
    List<TicketDTO> getTicketsForEvent(String eventId, Member loggedInMember);
    List<PerformerDTO> getPerformersForEvent(String eventId);
    
    // Bookmark related methods
    BaseResponseDto toggleEventBookmark(String eventId, Member member);
    BaseResponseDto removeEventBookmark(String eventId, Member member);
    List<SimpleEventDTO> getBookmarkedEvents(Member member);
    boolean isEventBookmarked(String eventId, Member member);
}
