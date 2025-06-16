package com.gig.service.impl;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import com.gig.exceptions.ApiException;
import com.gig.mappers.EventMapper;
import com.gig.models.Events;
import com.gig.models.Member;
import com.gig.repository.EventRepository;
import com.gig.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    public EventDTO getEventById(String id, Member loggedInMember) {
        Events event = eventRepository.findByIdAndIsDeletedFalse(id);
        if (event == null) {
            throw new ApiException(String.format(EVENT_NOT_FOUND, id));
        }
        EventDTO eventDTO = eventMapper.toDto(event, loggedInMember);
        eventMapper.afterToDto(event, eventDTO, loggedInMember);
        return eventDTO;
    }

    @Override
    public List<SimpleEventDTO> getAllEvents(int page, int size, Member loggedInMember) {
        int offset = Math.max(0, (page - 1) * size);
        List<Events> events = eventRepository.findAllByIsDeletedFalse(offset, size);
        
        // Early return if no logged in user or no events
        if (loggedInMember == null || events.isEmpty()) {
            return events.stream()
                    .map(eventMapper::toSimpleEventDTO)
                    .collect(Collectors.toList());
        }
        
        // Fetch all bookmarked event IDs for the logged-in user in a single query
        Set<UUID> bookmarkedEventIds = eventRepository.findBookmarkedEventIdsByMemberId(
            loggedInMember.getId().toString()
        );
        
        return events.stream()
                .map(event -> {
                    SimpleEventDTO dto = eventMapper.toSimpleEventDTO(event);
                    dto.setBookmarked(bookmarkedEventIds.contains(event.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SimpleEventDTO> getEventsByCategory(int page, int size, String category, String eventId, Member loggedInMember) {
        int offset = Math.max(0, (page - 1) * size);
        List<Events> events = eventRepository.findByCategoryAndIsDeletedFalse(category, offset, size, eventId);
        
        // Early return if no logged-in user or no events
        if (loggedInMember == null || events.isEmpty()) {
            return events.stream()
                    .map(eventMapper::toSimpleEventDTO)
                    .collect(Collectors.toList());
        }
        
        // Fetch all bookmarked event IDs for the logged-in user in a single query
        Set<UUID> bookmarkedEventIds = eventRepository.findBookmarkedEventIdsByMemberId(
            loggedInMember.getId().toString()
        );
        
        return events.stream()
                .map(event -> {
                    SimpleEventDTO dto = eventMapper.toSimpleEventDTO(event);
                    dto.setBookmarked(bookmarkedEventIds.contains(event.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countEventsByCategory(String category, String eventId) {
        return eventRepository.countByCategory(category, eventId);
    }

    @Override
    public long countAllEvents() {
        return eventRepository.countAllByIsDeletedFalse();
    }

    @Override
    public List<SimpleEventDTO> getUserEvents(int page, int size, Member loggedInMember) {
        int offset = Math.max(0, (page - 1) * size);
        List<Events> events = eventRepository.findByCreatedByAndIsDeletedFalse(loggedInMember.getId().toString(), offset, size);
        
        // Early return if no logged in user or no events
        if (loggedInMember == null || events.isEmpty()) {
            return events.stream()
                    .map(eventMapper::toSimpleEventDTO)
                    .collect(Collectors.toList());
        }
        
        // Fetch all bookmarked event IDs for the logged-in user in a single query
        Set<UUID> bookmarkedEventIds = eventRepository.findBookmarkedEventIdsByMemberId(
            loggedInMember.getId().toString()
        );
        
        return events.stream()
                .map(event -> {
                    SimpleEventDTO dto = eventMapper.toSimpleEventDTO(event);
                    dto.setBookmarked(bookmarkedEventIds.contains(event.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public long countUserEvents(Member loggedInMember) {
        return eventRepository.countByCreatedByAndIsDeletedFalse(loggedInMember.getId().toString());
    }



    private void validateTickets(List<TicketDTO> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            throw new ApiException(INVALID_TICKETS);
        }
        for (TicketDTO ticket : tickets) {
            if (ticket == null || ticket.getName() == null || ticket.getName().isEmpty() ||
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
                    performer.getRole() == null || performer.getRole().isEmpty()) {
                throw new ApiException(INVALID_PERFORMERS);
            }
        }
    }

    @Override
    public BaseResponseDto createEvent(EventDTO eventDTO, Member loggedInMember) {
        BaseResponseDto responseDto = new BaseResponseDto();
        validateEventDates(eventDTO);
        validateTickets(eventDTO.getTickets());
        validatePerformers(eventDTO.getPerformers());

        Events event = eventMapper.toEntity(eventDTO);
        event.setMember(loggedInMember);
        event.setCreatedBy(loggedInMember.getId());
        event.setCreationTimestamp(LocalDateTime.now());
        event.setUpdatedBy(loggedInMember.getId());
        event.setUpdateTimestamp(LocalDateTime.now());
        eventRepository.save(event);
        responseDto.setMessage("Event Created Successfully");
        return responseDto;
    }

    @Override
    public BaseResponseDto updateEvent(String id, EventDTO updatedEventDTO, Member loggedInMember) {
        BaseResponseDto responseDto = new BaseResponseDto();
        validateEventDates(updatedEventDTO);
        validateTickets(updatedEventDTO.getTickets());
        validatePerformers(updatedEventDTO.getPerformers());

        Events existingEvent = eventRepository.findByIdAndIsDeletedFalse(id);
        if (existingEvent == null) {
            throw new ApiException(String.format(EVENT_NOT_FOUND, id));
        }
        Events updatedEvent = eventMapper.toEntity(updatedEventDTO);

        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setStartDateTime(updatedEvent.getStartDateTime());
        existingEvent.setEndDateTime(updatedEvent.getEndDateTime());
        existingEvent.setTickets(updatedEvent.getTickets());
        existingEvent.setPerformers(updatedEvent.getPerformers());
        existingEvent.setUpdatedBy(loggedInMember.getId());
        existingEvent.setUpdateTimestamp(LocalDateTime.now());

        eventRepository.save(existingEvent);
        responseDto.setMessage("Event Updated Successfully");
        return responseDto;
    }

    @Override
    public void deleteEvent(String id, Member loggedInMember) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(id);
            event.setIsDeleted(true);
            eventRepository.save(event);
        } catch (Exception e) {
            throw new ApiException("Failed to delete event: " + e.getMessage(), e);
        }
    }
    @Override
    public List<TicketDTO> getTicketsForEvent(String eventId, Member loggedInMember) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            return eventMapper.toTicketDtoList(event.getTickets());
        } catch (Exception e) {
            throw new ApiException("Failed to fetch tickets for event: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PerformerDTO> getPerformersForEvent(String eventId) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            return eventMapper.toPerformerDtoList(event.getPerformers());
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

    @Override
    public BaseResponseDto toggleEventBookmark(String eventId, Member member) {
        BaseResponseDto response = new BaseResponseDto();
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            if (event == null) {
                throw new ApiException(String.format(EVENT_NOT_FOUND, eventId));
            }

            // Check if bookmark already exists
            boolean isBookmarked = event.getBookmarkedBy().contains(member);

            if (isBookmarked) {
                // Remove bookmark
                event.getBookmarkedBy().remove(member);
                response.setMessage("Event unbookmarked successfully");
            } else {
                // Add bookmark
                event.getBookmarkedBy().add(member);
                response.setMessage("Event bookmarked successfully");
            }

            eventRepository.save(event);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponseDto removeEventBookmark(String eventId, Member member) {
        BaseResponseDto response = new BaseResponseDto();
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            if (event == null) {
                throw new ApiException(String.format(EVENT_NOT_FOUND, eventId));
            }

            // Remove bookmark if it exists
            boolean removed = event.getBookmarkedBy().remove(member);
            if (removed) {
                eventRepository.save(event);
                response.setMessage("Bookmark removed successfully");
            } else {
                response.setMessage("Bookmark not found");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleEventDTO> getBookmarkedEvents(Member member) {
        try {
            // Fetch the member's bookmarked events
            List<Events> bookmarkedEvents = eventRepository.findBookmarkedEventsByMemberId(member.getId().toString());

            // Convert to SimpleEventDTO and ensure bookmarked flag is set to true
            return bookmarkedEvents.stream()
                    .map(event -> {
                        SimpleEventDTO dto = eventMapper.toSimpleEventDTO(event);
                        dto.setBookmarked(true);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException("Failed to fetch bookmarked events: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isEventBookmarked(String eventId, Member member) {
        Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
        if (event == null) {
            throw new ApiException(String.format(EVENT_NOT_FOUND, eventId));
        }
        return event.getBookmarkedBy().contains(member);
    }

    @Override
    public PageResponseDTO<SimpleEventDTO> discoverEvents(int page, int size, String location, boolean upcomingOnly, Member followedByMember) {
        
        // Calculate offset for pagination
        int offset = page * size;
        
        // Get events from repository
        List<Events> events = eventRepository.discoverEvents(offset, size, location, upcomingOnly, followedByMember != null ? followedByMember.getId().toString() : null);
        
        // Get total count for pagination
        long totalElements = eventRepository.countDiscoverEvents(
                location, 
                upcomingOnly, 
                followedByMember != null ? followedByMember.getId().toString() : null
        );
        
        // Convert to DTOs
        List<SimpleEventDTO> eventDTOs = events.stream()
                .map(event -> {
                    SimpleEventDTO dto = eventMapper.toSimpleEventDTO(event);
                    return dto;
                })
                .collect(Collectors.toList());
        
        // Create and return paginated response
        PageResponseDTO<SimpleEventDTO> response = new PageResponseDTO<>();
        response.setContent(eventDTOs);
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(totalElements);
        response.setTotalPages((int) Math.ceil((double) totalElements / size));
        response.setLast((page + 1) * size >= totalElements);
        return response;
    }
    
    @Override
    public PageResponseDTO<SimpleEventDTO> searchEvents(int page, int size, String category, String location, LocalDate startDate, LocalDate endDate, Double minPrice, Double maxPrice, String searchTerm, Member loggedInMember) {
            
        // Calculate offset for pagination
        int offset = page * size;
        
        // Get events from repository with all filters
        List<Events> events = eventRepository.searchEvents(category, location, startDate, endDate, minPrice, maxPrice, searchTerm, offset, size);
        
        // Get total count for pagination
        long totalElements = eventRepository.countSearchEvents(category, location, startDate, endDate, minPrice, maxPrice, searchTerm);
        
        // Convert to DTOs
        List<SimpleEventDTO> eventDTOs = events.stream()
                .map(eventMapper::toSimpleEventDTO)
                .collect(Collectors.toList());
        
        // Create and return paginated response
        PageResponseDTO<SimpleEventDTO> response = new PageResponseDTO<>();
        response.setContent(eventDTOs);
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(totalElements);
        response.setTotalPages((int) Math.ceil((double) totalElements / size));
        response.setLast((page + 1) * size >= totalElements);
        return response;
    }
}
