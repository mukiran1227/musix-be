package com.gig.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class LoginResponseDto extends BaseResponseDto{
    private String emailAddress;
    private String token;
    private UUID memberId;
    private Boolean isVerified=Boolean.FALSE;
    private String memberType;
}
