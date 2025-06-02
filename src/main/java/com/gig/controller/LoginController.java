package com.gig.controller;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CraftDto;
import com.gig.dto.ForgotPasswordDto;
import com.gig.dto.LoginDto;
import com.gig.dto.LoginResponseDto;
import com.gig.dto.ResetPasswordDto;
import com.gig.dto.VerifyRequestDto;
import com.gig.facade.LoginFacade;
import com.gig.service.LoginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth/member")
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginFacade loginFacade;

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDTO, HttpServletRequest httpServletRequest) {
        return loginFacade.login(loginDTO, httpServletRequest);
    }
    @PostMapping("/logout")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<BaseResponseDto> logout(HttpServletRequest request) {
        return loginService.logout(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponseDto> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        return loginFacade.forgotPassword(forgotPasswordDto);
    }

    @PostMapping(value = "/resend-otp")
    public ResponseEntity<BaseResponseDto> resendOtp(@RequestBody VerifyRequestDto verifyRequest) {
        return loginFacade.resendOtp(verifyRequest);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<LoginResponseDto> resetPassword(@RequestParam(value = "emailAddress") String emailAddress, @RequestBody ResetPasswordDto resetPasswordDto, HttpServletRequest httpServletRequest){
        return loginFacade.resetPassword(emailAddress, resetPasswordDto, httpServletRequest);
    }

    @PostMapping("/create-craft")
    public ResponseEntity<BaseResponseDto> createCraft(@RequestBody List<CraftDto> craftDto){
        return loginFacade.createCraft(craftDto);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<BaseResponseDto> verifyUserOtp(@RequestBody VerifyRequestDto verifyRequest) {
        return loginFacade.verifyOtp(verifyRequest);
    }

}
