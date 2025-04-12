package com.gig.facade;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.ChangePasswordDto;
import com.gig.dto.MemberDto;
import com.gig.dto.RegistrationDto;
import com.gig.dto.RegistrationResponseDto;
import com.gig.dto.UpdateMemberDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface MemberFacade {
    ResponseEntity<RegistrationResponseDto> createAccount(RegistrationDto registrationDto);

    ResponseEntity<MemberDto> getMember(String memberId, HttpServletRequest request);

    ResponseEntity<BaseResponseDto> updateMember(UpdateMemberDto memberDto, HttpServletRequest request);

    ResponseEntity<BaseResponseDto> updatePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request);
}
