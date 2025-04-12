package com.gig.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequestDto {
    private String emailAddress;
    private Integer otp;
}
