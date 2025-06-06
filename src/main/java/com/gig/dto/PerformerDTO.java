package com.gig.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PerformerDTO {
    private UUID id;
    private String name;
    private String role;
    private String imageUrl;
}
