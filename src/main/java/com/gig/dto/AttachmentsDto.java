package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttachmentsDto extends BaseResponseDto{
    private UUID id;
    private Boolean uploaded = Boolean.FALSE;
    private String fileName;
    private String contentType;
    private String uploadUrl;
}
