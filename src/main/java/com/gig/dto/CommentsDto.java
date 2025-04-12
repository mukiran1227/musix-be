package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentsDto extends BaseResponseDto {
    private UUID id;
    private String description;
    private String memberId;
    private String username;
    private String profileUrl;
}
