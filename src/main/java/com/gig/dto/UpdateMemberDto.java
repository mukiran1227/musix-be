package com.gig.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMemberDto extends BaseResponseDto{
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String location;
    private String imageUrl;
    private String bio;
    private String memberType;
    private String username;
    private String craft;
}
