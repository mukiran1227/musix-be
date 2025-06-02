package com.gig.facade;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CraftDto;
import com.gig.dto.ForgotPasswordDto;
import com.gig.dto.LoginDto;
import com.gig.dto.LoginResponseDto;
import com.gig.dto.ResetPasswordDto;
import com.gig.dto.VerifyRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoginFacade {
    ResponseEntity<LoginResponseDto> login(LoginDto loginDTO, HttpServletRequest httpServletRequest);

    ResponseEntity<BaseResponseDto> forgotPassword(ForgotPasswordDto forgotPasswordDto);

    ResponseEntity<BaseResponseDto> resendOtp(VerifyRequestDto verifyRequest);

    ResponseEntity<LoginResponseDto> resetPassword(String emailAddress, ResetPasswordDto resetPasswordDto, HttpServletRequest httpServletRequest);

    ResponseEntity<BaseResponseDto> createCraft(List<CraftDto> craftDto);

    ResponseEntity<BaseResponseDto> verifyOtp(VerifyRequestDto verifyRequest);
}
