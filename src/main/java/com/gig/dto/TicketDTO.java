package com.gig.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TicketDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;


    public TicketDTO(UUID id) {
        this.id = id;
    }


}
