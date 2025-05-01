package com.gig.service;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.ChangePasswordDto;
import com.gig.dto.RegistrationDto;
import com.gig.models.Member;

public interface MemberService {
    Member createAccount(RegistrationDto registrationDto);

    void updatePassword(ChangePasswordDto changePasswordDto, Member member);

    BaseResponseDto followOrUnfollow(String memberId, String followerId, Member loggedInMember);

    BaseResponseDto removeFollower(String memberId, String loggedInMember);
}
