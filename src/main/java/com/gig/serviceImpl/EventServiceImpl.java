package com.gig.serviceImpl;

import com.gig.dto.EventDTO;
import com.gig.dto.EventSearchDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.TicketDTO;
import com.gig.exceptions.ApiException;
import com.gig.mappers.EventMapper;
import com.gig.models.Events;
import com.gig.models.Performers;
import com.gig.models.Tickets;
import com.gig.repository.EventRepository;
import com.gig.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private static final String EVENT_NOT_FOUND = "Event not found with id: %s";
    private static final String INVALID_DATE_RANGE = "End date must be after start date";
    private static final String INVALID_TICKETS = "Invalid tickets provided";
    private static final String INVALID_PERFORMERS = "Invalid performers provided";

    @Override
    public EventDTO getEventById(String id) {
        Events event = eventRepository.findByIdAndIsDeletedFalse(id);
        if (event == null) {
            throw new ApiException(String.format(EVENT_NOT_FOUND, id));
        }
        return eventMapper.toDto(event);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Events> events = eventRepository.findAllByIsDeletedFalse();
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateTickets(List<TicketDTO> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            throw new ApiException(INVALID_TICKETS);
        }
        for (TicketDTO ticket : tickets) {
            if (ticket == null || ticket.getName() == null || ticket.getName().isEmpty() ||
                ticket.getDescription() == null || ticket.getDescription().isEmpty() ||
                ticket.getPrice() <= 0) {
                throw new ApiException(INVALID_TICKETS);
            }
        }
    }
    private void validatePerformers(List<PerformerDTO> performers) {
        if (performers == null || performers.isEmpty()) {
            throw new ApiException(INVALID_PERFORMERS);
        }
        for (PerformerDTO performer : performers) {
            if (performer == null || performer.getName() == null || performer.getName().isEmpty() ||
                performer.getRole() == null || performer.getRole().isEmpty() ||
                performer.getImageUrl() == null || performer.getImageUrl().isEmpty()) {
                throw new ApiException(INVALID_PERFORMERS);
            }
        }
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        validateEventDates(eventDTO);
        validateTickets(eventDTO.getTickets());
        validatePerformers(eventDTO.getPerformers());
        
        Events event = eventMapper.toEntity(eventDTO);
        if (eventDTO.getTickets() != null) {
            event.getTickets().addAll(eventDTO.getTickets().stream()
                .map(ticketDTO -> {
                    Tickets ticket = eventMapper.ticketDtoToEntity(ticketDTO);
                    event.getTickets().add(ticket);
                    return ticket;
                })
                .collect(Collectors.toSet()));
        }
        if (eventDTO.getPerformers() != null) {
            event.getPerformers().addAll(eventDTO.getPerformers().stream()
                .map(performerDTO -> {
                    Performers performer = eventMapper.performerDtoToEntity(performerDTO);
                    event.getPerformers().add(performer);
                    return performer;
                })
                .collect(Collectors.toSet()));
        }
        
        try {
            Events savedEvent = eventRepository.save(event);
            return eventMapper.toDto(savedEvent);
        } catch (Exception e) {
            throw new ApiException("Failed to create event: " + e.getMessage(), e);
        }
    }

    public List<EventDTO> getEventsByLocation(String location) {
        try {
            return eventRepository.findByLocationAndIsDeletedFalse(location)
                    .stream()
                    .map(eventMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException("Failed to fetch events by location: " + e.getMessage(), e);
        }
    }

    public List<EventDTO> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new ApiException(INVALID_DATE_RANGE);
        }
        
        try {
            return eventRepository.findByStartDateTimeBetweenNative(start, end)
                    .stream()
                    .map(eventMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException("Failed to fetch events by date range: " + e.getMessage(), e);
        }
    }

    public List<EventDTO> searchEvents(EventSearchDTO searchDTO) {
        try {
            List<Events> events = eventRepository.searchEvents(searchDTO.getCategories(),
                searchDTO.getLocation(),
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                searchDTO.getMinPrice(),
                searchDTO.getMaxPrice(),
                searchDTO.getSearchQuery()
            );
            return events.stream()
                    .map(eventMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException("Failed to search events: " + e.getMessage(), e);
        }
    }

    public EventDTO updateEvent(String id, EventDTO updatedEventDTO) {
        validateEventDates(updatedEventDTO);
        validateTickets(updatedEventDTO.getTickets());
        validatePerformers(updatedEventDTO.getPerformers());
        
        Events existingEvent = eventRepository.findByIdAndIsDeletedFalse(id);
        Events updatedEvent = eventMapper.toEntity(updatedEventDTO);
        
        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setStartDateTime(updatedEvent.getStartDateTime());
        existingEvent.setEndDateTime(updatedEvent.getEndDateTime());
        existingEvent.setCategory(updatedEvent.getCategory());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setCoverImageUrl(updatedEvent.getCoverImageUrl());
        existingEvent.setImageUrl(updatedEvent.getImageUrl());
        
        // Update relationships
        if (updatedEventDTO.getTickets() != null) {
            existingEvent.getTickets().clear();
            existingEvent.getTickets().addAll(updatedEventDTO.getTickets().stream()
                .map(ticketDTO -> {
                    Tickets ticket = eventMapper.ticketDtoToEntity(ticketDTO);
                    existingEvent.getTickets().add(ticket);
                    return ticket;
                })
                .collect(Collectors.toSet()));
        }
        if (updatedEventDTO.getPerformers() != null) {
            existingEvent.getPerformers().clear();
            existingEvent.getPerformers().addAll(updatedEventDTO.getPerformers().stream()
                .map(performerDTO -> {
                    Performers performer = eventMapper.performerDtoToEntity(performerDTO);
                    existingEvent.getPerformers().add(performer);
                    return performer;
                })
                .collect(Collectors.toSet()));
        }
        
        try {
            Events savedEvent = eventRepository.save(existingEvent);
            return eventMapper.toDto(savedEvent);
        } catch (Exception e) {
            throw new ApiException("Failed to update event: " + e.getMessage(), e);
        }
    }

    public void deleteEvent(String id) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(id);
            event.setIsDeleted(true);
            eventRepository.save(event);
        } catch (Exception e) {
            throw new ApiException("Failed to delete event: " + e.getMessage(), e);
        }
    }

    public List<TicketDTO> getTicketsForEvent(String eventId) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            return eventMapper.toTicketDtoList(new ArrayList<>(event.getTickets()));
        } catch (Exception e) {
            throw new ApiException("Failed to fetch tickets for event: " + e.getMessage(), e);
        }
    }

    public List<PerformerDTO> getPerformersForEvent(String eventId) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            return eventMapper.toPerformerDtoList(new ArrayList<>(event.getPerformers()));
        } catch (Exception e) {
            throw new ApiException("Failed to fetch performers for event: " + e.getMessage(), e);
        }
    }

    private void validateEventDates(EventDTO eventDTO) {
        if (eventDTO.getStartDateTime() != null && eventDTO.getEndDateTime() != null && 
            eventDTO.getStartDateTime().isAfter(eventDTO.getEndDateTime())) {
            throw new ApiException(INVALID_DATE_RANGE);
        }
    }
}
