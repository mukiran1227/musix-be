package com.gig.dto;

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
    private String imageUrl;
}
