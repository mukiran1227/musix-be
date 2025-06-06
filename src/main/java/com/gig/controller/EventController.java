package com.gig.controller;

import com.gig.dto.EventDTO;
import com.gig.dto.EventSearchDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.TicketDTO;
import com.gig.facade.EventFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Event Management", description = "APIs for managing events")
public class EventController {

    private final EventFacade eventFacade;

    @Autowired
    public EventController(EventFacade eventFacade) {
        this.eventFacade = eventFacade;
    }

    // CRUD Operations
    @PostMapping("/create")
    @Operation(summary = "Create a new event", description = "Creates a new event with the provided details")
    public ResponseEntity<EventDTO> createEvent(@Parameter(description = "Event details to be created") @RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventFacade.createEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Fetches an event by its unique identifier")
    public ResponseEntity<EventDTO> getEventById(@Parameter(description = "ID of the event to fetch") @PathVariable(value = "id") String id) {
        EventDTO event = eventFacade.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/fetch/events")
    @Operation(summary = "Get all events", description = "Fetches all active events")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventFacade.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event", description = "Updates an existing event with new details")
    public ResponseEntity<EventDTO> updateEvent(
            @Parameter(description = "ID of the event to update")
            @PathVariable String id,
            @Parameter(description = "Updated event details")
            @RequestBody EventDTO updatedEventDTO) {
        EventDTO updatedEvent = eventFacade.updateEvent(id, updatedEventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event", description = "Soft deletes an event by marking it as deleted")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID of the event to delete")
            @PathVariable String id) {
        eventFacade.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Search Operations
    @PostMapping("/search")
    @Operation(summary = "Search events", description = "Search events with various filters")
    public ResponseEntity<List<EventDTO>> searchEvents(@Parameter(description = "Search parameters") @RequestBody EventSearchDTO searchDTO) {
        if (searchDTO == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Search parameters cannot be null");
        }
        
        List<EventDTO> events = eventFacade.searchEvents(searchDTO);
        return ResponseEntity.ok(events);
    }

    // Event Relationships
    @GetMapping("/{id}/tickets")
    @Operation(summary = "Get tickets for event", description = "Fetches all tickets for a specific event")
    public ResponseEntity<List<TicketDTO>> getTicketsForEvent(@Parameter(description = "ID of the event") @PathVariable(value = "id") String id) {
        List<TicketDTO> tickets = eventFacade.getTicketsForEvent(id);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}/performers")
    @Operation(summary = "Get performers for event", description = "Fetches all performers for a specific event")
    public ResponseEntity<List<PerformerDTO>> getPerformersForEvent(@Parameter(description = "ID of the event") @PathVariable String id) {
        List<PerformerDTO> performers = eventFacade.getPerformersForEvent(id);
        return ResponseEntity.ok(performers);
    }
}
