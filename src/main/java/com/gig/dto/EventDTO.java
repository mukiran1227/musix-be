package com.gig.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EventDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String category;
    private String location;
    private List<AttachmentsDto> coverImageUrl;
    private List<AttachmentsDto> imageUrl;
    private List<TicketDTO> tickets;
    private List<PerformerDTO> performers;
    private String instructions;
    private String termsAndConditions;
    private LocalDateTime creationTimestamp;
    private String createdBy;
    private Boolean bookmarked = false;
}
