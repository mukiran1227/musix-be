package com.gig.service;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.models.Member;

import java.time.LocalDate;
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
    
    /**
     * Discovers events based on various filters
     * @param page Page number (0-based)
     * @param size Page size
     * @param location Location to search near (can be null)
     * @param upcomingOnly Whether to only include upcoming events
     * @param followedByMember Member whose followed users' events to include (can be null)
     * @return Page of events matching the criteria
     */
    PageResponseDTO<SimpleEventDTO> discoverEvents(int page, int size, String location, boolean upcomingOnly, Member followedByMember);
    
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
     * @param loggedInMember Currently logged in member (optional)
     * @return Page of events matching the criteria
     */
    PageResponseDTO<SimpleEventDTO> searchEvents(int page, int size, String category, String location, LocalDate startDate, LocalDate endDate, Double minPrice, Double maxPrice, String searchTerm, Member loggedInMember);
}
