package com.gig.facade.impl;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.exceptions.ApiException;
import com.gig.facade.EventFacade;
import com.gig.models.Member;
import com.gig.service.EventService;
import com.gig.applicationUtilities.ApplicationUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventFacadeImpl implements EventFacade {

    @Autowired
    private EventService eventService;

    @Autowired
    private ApplicationUtilities applicationUtilities;

    @Override
    public BaseResponseDto createEvent(EventDTO eventDTO, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.createEvent(eventDTO, loggedInMember);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return responseDto;
        }
    }

    @Override
    public EventDTO getEventById(String id, HttpServletRequest request) {
        try {
            applicationUtilities.getLoggedInUser(request);
            return eventService.getEventById(id);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<EventDTO> getEventsByCategory(int page, int size, String category, HttpServletRequest request) {
        try {
            applicationUtilities.getLoggedInUser(request);
            long total = eventService.countEventsByCategory(category);
            List<EventDTO> events = eventService.getEventsByCategory(page, size, category);
            PageResponseDTO<EventDTO> response = new PageResponseDTO<>();
            response.setContent(events);
            response.setPageNumber(page);
            response.setPageSize(size);
            response.setTotalElements(total);
            response.setTotalPages((int) Math.ceil((double) total / size));
            response.setLast(page >= (response.getTotalPages() - 1));
            return response;
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<EventDTO> getAllEvents(int page, int size, HttpServletRequest request) {
        try {
            applicationUtilities.getLoggedInUser(request);
            List<EventDTO> events = eventService.getAllEvents(page, size);
            long totalElements = eventService.countAllEvents();
            return createPageResponse(events, page, size, totalElements);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<EventDTO> getUserPosts(int page, int size, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            List<EventDTO> events = eventService.getUserPosts(page, size, loggedInMember);
            long totalElements = eventService.countUserPosts(loggedInMember);
            return createPageResponse(events, page, size, totalElements);
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

    private PageResponseDTO<EventDTO> createPageResponse(List<EventDTO> content, int page, int size, long totalElements) {
        PageResponseDTO<EventDTO> response = new PageResponseDTO<>();
        response.setContent(content);
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(totalElements);
        response.setTotalPages((int) Math.ceil((double) totalElements / size));
        response.setLast(page >= response.getTotalPages() - 1);
        return response;
    }

    @Override
    public List<PerformerDTO> getPerformersForEvent(String eventId, HttpServletRequest request) {
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.getPerformersForEvent(eventId);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }
}
