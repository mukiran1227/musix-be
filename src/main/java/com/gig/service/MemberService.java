package com.gig.service;

import com.gig.dto.ChangePasswordDto;
import com.gig.dto.RegistrationDto;
import com.gig.models.Member;

public interface MemberService {
    Member createAccount(RegistrationDto registrationDto);

    void updatePassword(ChangePasswordDto changePasswordDto, Member member);
}
