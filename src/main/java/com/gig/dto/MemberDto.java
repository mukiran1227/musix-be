package com.gig.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MemberDto extends BaseResponseDto{
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String location;
    private Boolean isVerified=Boolean.FALSE;
    private int followersCount;
    private String imageUrl;
    private String bio;
    private String memberType;
    private String username;
}
