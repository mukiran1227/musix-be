package com.gig.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SimpleEventDTO {
    private UUID id;
    private String name;
    private String description;
    private List<AttachmentsDto> coverImageUrl;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private String category;
    private double minPrice;
    private double maxPrice;
}
