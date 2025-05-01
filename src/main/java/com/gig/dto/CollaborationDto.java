package com.gig.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CollaborationDto extends BaseResponseDto{
    private UUID id;
    private String name;
    private String bio;
    private String inviteMessage;
    private String emailAddress;
    private String imageUrl;
    private String clientLookingFor;
    private String artistLookingFor;
}
