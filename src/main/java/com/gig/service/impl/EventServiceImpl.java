package com.gig.service.impl;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PerformerDTO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

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
        EventDTO  eventDTO = EventMapper.INSTANCE.toDto(event);
        EventMapper.INSTANCE.afterToDto(event,eventDTO);
        return eventDTO;

    }

    @Override
    public List<EventDTO> getAllEvents(int page, int size) {
        int offset = (page - 1) * size;
        List<Events> events = eventRepository.findAllByIsDeletedFalse(offset, size);
        return events.stream()
                .map(event -> {
                    EventDTO dto = EventMapper.INSTANCE.toDto(event);
                    EventMapper.INSTANCE.afterToDto(event, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByCategory(int page, int size, String category, String eventId) {
        int offset = (page - 1) * size;
        List<Events> events = eventRepository.findByCategoryAndIsDeletedFalse(category, offset, size,eventId);
        return events.stream()
                .map(event -> {
                    EventDTO dto = EventMapper.INSTANCE.toDto(event);
                    EventMapper.INSTANCE.afterToDto(event, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countEventsByCategory(String category) {
        return eventRepository.countByCategory(category);
    }

    @Override
    public long countAllEvents() {
        return eventRepository.countAllByIsDeletedFalse();
    }

    @Override
    public List<EventDTO> getUserEvents(int page, int size, Member loggedInMember) {
        int offset = (page - 1) * size;
        List<Events> events = eventRepository.findByCreatedByAndIsDeletedFalse(loggedInMember.getId().toString(), offset, size);
        return events.stream()
                .map(event -> {
                    EventDTO dto = EventMapper.INSTANCE.toDto(event);
                    EventMapper.INSTANCE.afterToDto(event, dto);
                    return dto;
                })
                .toList();
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

    @Override
    public BaseResponseDto createEvent(EventDTO eventDTO, Member loggedInMember) {
        BaseResponseDto responseDto = new BaseResponseDto();
        validateEventDates(eventDTO);
        validateTickets(eventDTO.getTickets());
        validatePerformers(eventDTO.getPerformers());

        Events event = EventMapper.INSTANCE.toEntity(eventDTO);
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
        Events updatedEvent = EventMapper.INSTANCE.toEntity(updatedEventDTO);

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
            return EventMapper.INSTANCE.toTicketDtoList(event.getTickets());
        } catch (Exception e) {
            throw new ApiException("Failed to fetch tickets for event: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PerformerDTO> getPerformersForEvent(String eventId) {
        try {
            Events event = eventRepository.findByIdAndIsDeletedFalse(eventId);
            return EventMapper.INSTANCE.toPerformerDtoList(event.getPerformers());
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
