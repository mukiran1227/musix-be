package com.gig.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MemberDto extends BaseResponseDto{
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String city;
    private String craft;
    private String intentOfUse;
    private Boolean isVerified=Boolean.FALSE;
    private int followersCount;
    private String imageUrl;
    private String bio;
    private String clientLookingFor;
    private String artistLookingFor;
    private String coverImageUrl;
}
