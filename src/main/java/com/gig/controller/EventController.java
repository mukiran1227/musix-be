package com.gig.controller;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.facade.EventFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<BaseResponseDto> createEvent(@Parameter(description = "Event details to be created") @RequestBody EventDTO eventDTO, HttpServletRequest request) {
        BaseResponseDto responseDto = eventFacade.createEvent(eventDTO, request);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Fetches an event by its unique identifier")
    public ResponseEntity<EventDTO> getEventById(
            @Parameter(description = "ID of the event to fetch") @PathVariable(value = "id") String id,
            HttpServletRequest request) {
        EventDTO event = eventFacade.getEventById(id, request);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get events by category", description = "Fetches events filtered by category with pagination")
    public ResponseEntity<PageResponseDTO<EventDTO>> getEventsByCategory(
            @Parameter(description = "Category to filter events by") @PathVariable(value = "category") String category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        PageResponseDTO<EventDTO> response = eventFacade.getEventsByCategory(page, size, category, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetch/events")
    @Operation(summary = "Get all events", description = "Fetches all active events with pagination")
    public ResponseEntity<PageResponseDTO<EventDTO>> getAllEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        PageResponseDTO<EventDTO> events = eventFacade.getAllEvents(page, size, request);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/user/posts")
    @Operation(summary = "Get logged-in user's posts", description = "Fetches all posts created by the logged-in user")
    public ResponseEntity<PageResponseDTO<EventDTO>> getUserPosts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        PageResponseDTO<EventDTO> events = eventFacade.getUserPosts(page, size, request);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event", description = "Updates an existing event with new details")
    public ResponseEntity<BaseResponseDto> updateEvent(
            @Parameter(description = "ID of the event to update")
            @PathVariable String id,
            @Parameter(description = "Updated event details")
            @RequestBody EventDTO updatedEventDTO,
            HttpServletRequest request) {
        BaseResponseDto updatedEvent = eventFacade.updateEvent(id, updatedEventDTO, request);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event", description = "Soft deletes an event by marking it as deleted")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID of the event to delete")
            @PathVariable String id,
            HttpServletRequest request) {
        eventFacade.deleteEvent(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tickets")
    @Operation(summary = "Get tickets for event", description = "Fetches all tickets for a specific event")
    public ResponseEntity<List<TicketDTO>> getTicketsForEvent(
            @Parameter(description = "ID of the event") @PathVariable(value = "id") String id,
            HttpServletRequest request) {
        List<TicketDTO> tickets = eventFacade.getTicketsForEvent(id, request);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}/performers")
    @Operation(summary = "Get performers for event", description = "Fetches all performers for a specific event")
    public ResponseEntity<List<PerformerDTO>> getPerformersForEvent(
            @Parameter(description = "ID of the event") @PathVariable String id,
            HttpServletRequest request) {
        List<PerformerDTO> performers = eventFacade.getPerformersForEvent(id, request);
        return ResponseEntity.ok(performers);
    }
}
