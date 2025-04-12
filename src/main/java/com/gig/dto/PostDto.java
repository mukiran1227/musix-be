package com.gig.dto;

import com.gig.models.Attachments;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostDto extends BaseResponseDto{
    private UUID id;
    private String description;
    private LocalDateTime creationTimestamp;
    private List<AttachmentsDto> attachments = new ArrayList<>();
    private String memberId;
    private String username;
    private String profileUrl;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private Boolean isLiked = Boolean.FALSE;
}
