package com.gig.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TicketDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;
}
