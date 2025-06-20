package com.gig.controller;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EventDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.dto.PerformerDTO;
import com.gig.dto.SimpleEventDTO;
import com.gig.dto.TicketDTO;
import com.gig.facade.EventFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * EventController
 */
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
    public ResponseEntity<PageResponseDTO<SimpleEventDTO>> getEventsByCategory(
            @Parameter(description = "Category to filter events by") @PathVariable(value = "category") String category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "event id") @RequestParam(value = "eventId",required = false) String eventId,
            HttpServletRequest request) {
        PageResponseDTO<SimpleEventDTO> events = eventFacade.getEventsByCategory(page, size, category, eventId, request);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/fetch/events")
    @Operation(summary = "Get all events", description = "Fetches all active events with pagination")
    public ResponseEntity<PageResponseDTO<SimpleEventDTO>> getAllEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        PageResponseDTO<SimpleEventDTO> events = eventFacade.getAllEvents(page, size, request);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/user/events")
    @Operation(summary = "Get logged-in user's events", description = "Fetches all events created by the logged-in user")
    public ResponseEntity<PageResponseDTO<SimpleEventDTO>> getUserEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        PageResponseDTO<SimpleEventDTO> events = eventFacade.getUserEvents(page, size, request);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/discover")
    @Operation(summary = "Discover events", description = "Fetches events based on location, upcoming status, and followed users")
    public ResponseEntity<PageResponseDTO<SimpleEventDTO>> discoverEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Location to search near (e.g., 'New York')") @RequestParam(required = false) String location,
            @Parameter(description = "Only show upcoming events") @RequestParam(defaultValue = "false") boolean upcomingOnly,
            @Parameter(description = "Only show events from followed users") @RequestParam(defaultValue = "false") boolean followingOnly,
            HttpServletRequest request) {
        PageResponseDTO<SimpleEventDTO> events = eventFacade.discoverEvents(page, size, location, upcomingOnly, followingOnly, request);
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Search events with multiple filters")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<SimpleEventDTO>> searchEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Category to filter by") @RequestParam(required = false) String category,
            @Parameter(description = "Location to filter by") @RequestParam(required = false) String location,
            @Parameter(description = "Start date to filter from (ISO date format: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date to filter to (ISO date format: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Minimum ticket price") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum ticket price") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Search term for name, location, or description") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        PageResponseDTO<SimpleEventDTO> events = eventFacade.searchEvents(page, size, category, location, startDate, endDate, minPrice, maxPrice, searchTerm, request);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event", description = "Updates an existing event with new details")
    public ResponseEntity<BaseResponseDto> updateEvent(
            @PathVariable String id,
            @Parameter(description = "Updated event details")
            @RequestBody EventDTO eventDTO,
            HttpServletRequest request) {
        BaseResponseDto response = eventFacade.updateEvent(id, eventDTO, request);
        return ResponseEntity.ok(response);
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

    @GetMapping("/{eventId}/performers")
    @Operation(summary = "Get performers for event", description = "Fetches all performers for a specific event")
    public ResponseEntity<List<PerformerDTO>> getPerformersForEvent(
            @PathVariable String eventId,
            HttpServletRequest request) {
        return ResponseEntity.ok(eventFacade.getPerformersForEvent(eventId, request));
    }

    @PostMapping("/{eventId}/bookmark/toggle")
    @Operation(summary = "Toggle event bookmark", description = "Toggles the bookmark status of an event for the current user")
    public ResponseEntity<BaseResponseDto> toggleEventBookmark(
            @PathVariable String eventId,
            HttpServletRequest request) {
        return ResponseEntity.ok(eventFacade.toggleEventBookmark(eventId, request));
    }

    @DeleteMapping("/{eventId}/bookmark")
    @Operation(summary = "Remove event bookmark", description = "Removes the bookmark from an event for the current user")
    public ResponseEntity<BaseResponseDto> removeEventBookmark(
            @PathVariable String eventId,
            HttpServletRequest request) {
        BaseResponseDto response = eventFacade.removeEventBookmark(eventId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmarks")
    @Operation(summary = "Get bookmarked events", description = "Retrieves a list of events bookmarked by the current user")
    public ResponseEntity<List<SimpleEventDTO>> getBookmarkedEvents(HttpServletRequest request) {
        List<SimpleEventDTO> events = eventFacade.getBookmarkedEvents(request);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/is-bookmarked")
    @Operation(summary = "Check if event is bookmarked", description = "Checks if the specified event is bookmarked by the current user")
    public ResponseEntity<Boolean> isEventBookmarked(
            @PathVariable String eventId,
            HttpServletRequest request) {
        return ResponseEntity.ok(eventFacade.isEventBookmarked(eventId, request));
    }
}
