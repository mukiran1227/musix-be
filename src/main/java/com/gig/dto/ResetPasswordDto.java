package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private Integer otp;
    private String password;
    private String reCheckPassword;
}
