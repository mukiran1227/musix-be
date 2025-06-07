package com.gig.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TicketDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private boolean deleted;
    private LocalDateTime creationTimestamp;
    private String createdBy;
    private LocalDateTime updateTimestamp;
    private String updatedBy;
}
