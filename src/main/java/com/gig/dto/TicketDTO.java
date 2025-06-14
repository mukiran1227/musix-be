package com.gig.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;

    public TicketDTO(UUID id) {
        this.id = id;
    }
}
