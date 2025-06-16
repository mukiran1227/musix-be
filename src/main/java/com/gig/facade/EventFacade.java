package com.gig.facade;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import java.time.LocalDate;
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
    
    PageResponseDTO<SimpleEventDTO> discoverEvents(int page, int size, String location, boolean upcomingOnly, boolean followingOnly, HttpServletRequest request);
    
    /**
     * Search events with multiple filters
     * @param page Page number (0-based)
     * @param size Page size
     * @param category Category to filter by (optional)
     * @param location Location to filter by (optional)
     * @param startDate Start date to filter from (optional)
     * @param endDate End date to filter to (optional)
     * @param minPrice Minimum ticket price (optional)
     * @param maxPrice Maximum ticket price (optional)
     * @param searchTerm Term to search in name, location, or description (optional)
     * @param request HTTP request for authentication
     * @return Page of events matching the criteria
     */
    PageResponseDTO<SimpleEventDTO> searchEvents(int page, int size, String category, String location, LocalDate startDate, LocalDate endDate, Double minPrice, Double maxPrice, String searchTerm, HttpServletRequest request);
}
