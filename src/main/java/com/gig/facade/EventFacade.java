package com.gig.facade;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.PageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EventFacade {
    BaseResponseDto createEvent(EventDTO eventDTO, HttpServletRequest request);
    EventDTO getEventById(String id, HttpServletRequest request);
    PageResponseDTO<EventDTO> getAllEvents(int page, int size, HttpServletRequest request);
    PageResponseDTO<EventDTO> getUserEvents(int page, int size, HttpServletRequest request);
    BaseResponseDto updateEvent(String id, EventDTO updatedEventDTO, HttpServletRequest request);
    void deleteEvent(String id, HttpServletRequest request);
    List<TicketDTO> getTicketsForEvent(String eventId, HttpServletRequest request);
    List<PerformerDTO> getPerformersForEvent(String eventId, HttpServletRequest request);
    PageResponseDTO<EventDTO> getEventsByCategory(int page, int size, String category, String eventId, HttpServletRequest request);
}
