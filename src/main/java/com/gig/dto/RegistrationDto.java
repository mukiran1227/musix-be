package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegistrationDto {
    private UUID id;
    private String emailAddress;
    private String password;
    private String reCheckPassword;
}
