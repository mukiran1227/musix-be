package com.gig.facade.impl;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.mappers.EventMapper;
import java.util.List;
import java.util.stream.Collectors;
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
    public PageResponseDTO<SimpleEventDTO> getEventsByCategory(int page, int size, String category, String eventId, HttpServletRequest request) {
        try {
            applicationUtilities.getLoggedInUser(request);
            long total = eventService.countEventsByCategory(category);
            List<SimpleEventDTO> events = eventService.getEventsByCategory(page, size, category,eventId);
            return applicationUtilities.createPageResponse(events,page, size,total);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResponseDTO<SimpleEventDTO> getAllEvents(int page, int size, HttpServletRequest request) {
        try {
            applicationUtilities.getLoggedInUser(request);
            List<SimpleEventDTO> events = eventService.getAllEvents(page, size);
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
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            return eventService.getPerformersForEvent(eventId);
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage(), ex);
        }
    }
}
