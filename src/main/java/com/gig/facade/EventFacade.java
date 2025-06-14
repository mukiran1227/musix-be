package com.gig.facade;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EventFacade {
    BaseResponseDto createEvent(EventDTO eventDTO, HttpServletRequest request);
    EventDTO getEventById(String id, HttpServletRequest request);
    PageResponseDTO<SimpleEventDTO> getAllEvents(int page, int size, HttpServletRequest request);
    PageResponseDTO<SimpleEventDTO> getUserEvents(int page, int size, HttpServletRequest request);
    BaseResponseDto updateEvent(String id, EventDTO updatedEventDTO, HttpServletRequest request);
    void deleteEvent(String id, HttpServletRequest request);
    List<TicketDTO> getTicketsForEvent(String eventId, HttpServletRequest request);
    List<PerformerDTO> getPerformersForEvent(String eventId, HttpServletRequest request);
    PageResponseDTO<SimpleEventDTO> getEventsByCategory(int page, int size, String category, String eventId, HttpServletRequest request);
    
    // Bookmark related methods
    BaseResponseDto toggleEventBookmark(String eventId, HttpServletRequest request);
    List<SimpleEventDTO> getBookmarkedEvents(HttpServletRequest request);
    boolean isEventBookmarked(String eventId, HttpServletRequest request);
    BaseResponseDto removeEventBookmark(String eventId, HttpServletRequest request);
}
