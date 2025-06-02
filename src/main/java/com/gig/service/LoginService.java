package com.gig.service;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.EmailDto;
import com.gig.dto.LoginDto;
import com.gig.dto.LoginResponseDto;
import com.gig.dto.ResetPasswordDto;
import com.gig.enums.LoginStatusEnum;
import com.gig.models.Login;
import com.gig.models.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LoginService {
    Login findByTokenAndStatus(String authToken, LoginStatusEnum loginStatusEnum);

    Login saveLoginDetails(HttpServletRequest request, Login login, Member member);


    ResponseEntity<BaseResponseDto> logout(HttpServletRequest request);

    Login login(LoginDto loginDTO, HttpServletRequest httpServletRequest, LoginResponseDto loginResponseDto);

    BaseResponseDto generateOtp(String username, BaseResponseDto baseResponseDto, Member member, EmailDto emailDto);

    Map<String, Object> verifyOtp(Member member, ResetPasswordDto resetPasswordDto);
}
