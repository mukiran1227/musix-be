package com.gig.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PerformerDTO extends BaseResponseDto {
    private UUID id;
    private String name;
    private String role;
    private String imageUrl;
    private Boolean deleted;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private UUID createdBy;
    private UUID updatedBy;
}
