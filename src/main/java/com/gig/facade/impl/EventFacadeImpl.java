package com.gig.facade.impl;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.exceptions.ApiException;
import com.gig.facade.EventFacade;
import com.gig.models.Member;
import com.gig.service.EventService;
import com.gig.applicationUtilities.ApplicationUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of EventFacade interface
 */
@Component
public class EventFacadeImpl implements EventFacade {

    private final EventService eventService;
    private final ApplicationUtilities applicationUtilities;

    public EventFacadeImpl(EventService eventService, ApplicationUtilities applicationUtilities) {
        this.eventService = eventService;
        this.applicationUtilities = applicationUtilities;
    }

    @Override
    public BaseResponseDto createEvent(EventDTO eventDTO, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.createEvent(eventDTO, loggedInMember);
        } catch (Exception ex) {
            BaseResponseDto response = new BaseResponseDto();
            response.setMessage(ex.getMessage());
            return response;
        }
    }

    @Override
    public EventDTO getEventById(String id, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.getEventById(id, loggedInMember);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<SimpleEventDTO> getEventsByCategory(int page, int size, String category, String eventId, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            List<SimpleEventDTO> events = eventService.getEventsByCategory(page, size, category, eventId, loggedInMember);
            long totalElements = eventService.countEventsByCategory(category, eventId);
            return applicationUtilities.createPageResponse(events, page, size, totalElements);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<SimpleEventDTO> getAllEvents(int page, int size, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            List<SimpleEventDTO> events = eventService.getAllEvents(page, size, loggedInMember);
            long totalElements = eventService.countAllEvents();
            return applicationUtilities.createPageResponse(events, page, size, totalElements);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<SimpleEventDTO> getUserEvents(int page, int size, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            List<SimpleEventDTO> events = eventService.getUserEvents(page, size, loggedInMember);
            long totalElements = eventService.countUserEvents(loggedInMember);
            return applicationUtilities.createPageResponse(events, page, size, totalElements);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }


    @Override
    public BaseResponseDto updateEvent(String id, EventDTO updatedEventDTO, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.updateEvent(id, updatedEventDTO, loggedInMember);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteEvent(String id, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            eventService.deleteEvent(id, loggedInMember);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<TicketDTO> getTicketsForEvent(String eventId, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.getTicketsForEvent(eventId, loggedInMember);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }


    @Override
    public List<PerformerDTO> getPerformersForEvent(String eventId, HttpServletRequest request) {
        try {
            // No need to get the logged-in user for this operation
            return eventService.getPerformersForEvent(eventId);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public BaseResponseDto toggleEventBookmark(String eventId, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.toggleEventBookmark(eventId, loggedInMember);
        } catch (Exception ex) {
            BaseResponseDto response = new BaseResponseDto();
            response.setMessage(ex.getMessage());
            return response;
        }
    }

    @Override
    public BaseResponseDto removeEventBookmark(String eventId, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.removeEventBookmark(eventId, loggedInMember);
        } catch (Exception ex) {
            BaseResponseDto response = new BaseResponseDto();
            response.setMessage(ex.getMessage());
            return response;
        }
    }

    @Override
    public List<SimpleEventDTO> getBookmarkedEvents(HttpServletRequest request) {
        Member loggedInMember = applicationUtilities.getLoggedInUser(request);
        return eventService.getBookmarkedEvents(loggedInMember);
    }
    
    @Override
    public PageResponseDTO<SimpleEventDTO> discoverEvents(int page, int size, String location, boolean upcomingOnly, boolean followingOnly, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.discoverEvents(page, size, location, upcomingOnly, followingOnly ? loggedInMember : null);
        } catch (Exception ex) {
            throw new ApiException("Failed to retrieve bookmarked events: " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public PageResponseDTO<SimpleEventDTO> searchEvents(int page, int size, String category, String location, LocalDate startDate, LocalDate endDate, Double minPrice, Double maxPrice, String searchTerm, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.searchEvents(page, size, category, location, startDate, endDate, minPrice, maxPrice, searchTerm, loggedInMember);
        } catch (Exception ex) {
            throw new ApiException("Failed to search events: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isEventBookmarked(String eventId, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.isEventBookmarked(eventId, loggedInMember);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }
}
