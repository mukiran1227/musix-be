package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AttachmentsDto extends BaseResponseDto {
    private UUID id;
    private Boolean uploaded = Boolean.FALSE;
    private String fileName;
    private String contentType;
    private String uploadUrl;
    private Boolean deleted;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private UUID createdBy;
    private UUID updatedBy;
}
