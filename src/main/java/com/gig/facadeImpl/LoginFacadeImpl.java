package com.gig.facadeImpl;

import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.ForgotPasswordDto;
import com.gig.dto.LoginDto;
import com.gig.dto.LoginResponseDto;
import com.gig.dto.ResetPasswordDto;
import com.gig.dto.VerifyRequestDto;
import com.gig.exceptions.ApiException;
import com.gig.facade.LoginFacade;
import com.gig.models.Login;
import com.gig.models.Member;
import com.gig.repository.MemberRepository;
import com.gig.service.LoginService;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.gig.applicationUtilities.ApplicationConstants.ENTERED_OTP_HAS_EXPIRED_PLEASE_CLICK_ON_RESEND_BUTTON_TO_GET_A_NEW_OTP;
import static com.gig.applicationUtilities.ApplicationConstants.EXPIRED;
import static com.gig.applicationUtilities.ApplicationConstants.INVALID;
import static com.gig.applicationUtilities.ApplicationConstants.INVALID_OTP;
import static com.gig.applicationUtilities.ApplicationConstants.INVALID_OTP_PLEASE_ENTER_A_VALID_OTP;
import static com.gig.applicationUtilities.ApplicationConstants.MEMBER_NOT_FOUND;
import static com.gig.applicationUtilities.ApplicationConstants.OLD_PASSWORD_NEW_PASSWORD_ARE_SAME;
import static com.gig.applicationUtilities.ApplicationConstants.OTP_VERIFIED_SUCCESSFULLY;
import static com.gig.applicationUtilities.ApplicationConstants.PASSWORDS_DO_NOT_MATCH;
import static com.gig.applicationUtilities.ApplicationConstants.PASSWORD_CHANGED;
import static com.gig.applicationUtilities.ApplicationConstants.SUCCESS;
import static com.gig.applicationUtilities.ApplicationConstants.USER_DOES_NOT_EXIST;

@Component
public class LoginFacadeImpl implements LoginFacade {

    private final Logger logger = LoggerFactory.getLogger(LoginFacadeImpl.class);

    @Autowired
    private LoginService loginService;
    
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationUtilities applicationUtilities;
    
    @Override
    public ResponseEntity<LoginResponseDto> login(LoginDto loginDTO, HttpServletRequest httpServletRequest) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try{
            Login login = loginService.login(loginDTO,httpServletRequest,loginResponseDto);
            if(ObjectUtils.isNotEmpty(login)) {
                Member member = login.getMember();
                if (Objects.nonNull(member)) {
                    loginResponseDto.setEmailAddress(member.getEmailAddress());
                    loginResponseDto.setMemberId(member.getId());
                    loginResponseDto.setToken(login.getToken());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            loginResponseDto.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(loginResponseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(loginResponseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String emailAddress = forgotPasswordDto.getEmailAddress();
        Member member = memberRepository.findByEmailAddress(emailAddress,false);
        if (ObjectUtils.isEmpty(member)) {
            logger.debug("LoginRegistrationServiceImpl::forgotPassword: {}", USER_DOES_NOT_EXIST);
            throw new ApiException(USER_DOES_NOT_EXIST);
        } else {
            String username = forgotPasswordDto.getEmailAddress().toLowerCase();
            baseResponseDto = loginService.generateOtp(username, baseResponseDto, member);
            logger.debug("LoginRegistrationServiceImpl::forgotPassword: Reset Password mail is sent to {}", emailAddress);
        }
        return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> resendOtp(VerifyRequestDto verifyRequest) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            Member member = memberRepository.findByEmailAddress(verifyRequest.getEmailAddress(), false);
            if(ObjectUtils.isNotEmpty(member)) {
                baseResponseDto = loginService.generateOtp(verifyRequest.getEmailAddress(), baseResponseDto, member);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getLocalizedMessage());
        }
        return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LoginResponseDto> resetPassword(String emailAddress, ResetPasswordDto resetPasswordDto, HttpServletRequest httpServletRequest) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try{
            Member member = memberRepository.findByEmailAddressOtp(emailAddress,resetPasswordDto.getOtp(), false);
            Assert.notNull(member, INVALID_OTP);
            if(ObjectUtils.isNotEmpty(member)) {
                Assert.isTrue(StringUtils.equals(resetPasswordDto.getPassword(), resetPasswordDto.getReCheckPassword()), PASSWORDS_DO_NOT_MATCH);
                logger.debug("LoginRegistrationFacadeImpl::verifyOtp :: validating the OTP");
                Map<String, Object> resource = loginService.verifyOtp(member, resetPasswordDto);
                if(resource.get(SUCCESS).equals(true)) {
                    loginResponseDto.setMessage(OTP_VERIFIED_SUCCESSFULLY);
                    loginResponseDto = setPassword(resetPasswordDto, httpServletRequest,emailAddress);
                    logger.debug("LoginRegistrationFacadeImpl::verifyOtp :: "+resetPasswordDto.getOtp()+" OTP verified successfully.");
                } else if(resource.get(EXPIRED).equals(true)) {
                    loginResponseDto.setMessage(ENTERED_OTP_HAS_EXPIRED_PLEASE_CLICK_ON_RESEND_BUTTON_TO_GET_A_NEW_OTP);
                    logger.debug("LoginRegistrationFacadeImpl::verifyOtp ::Entered OTP "+resetPasswordDto.getOtp()+"has expired, please click on resend button to get a new OTP.");
                    return new ResponseEntity<>(loginResponseDto, HttpStatus.BAD_REQUEST);
                } else if(resource.get(INVALID).equals(true)) {
                    loginResponseDto.setMessage(INVALID_OTP_PLEASE_ENTER_A_VALID_OTP);
                    logger.debug("LoginRegistrationFacadeImpl::verifyOtp ::Invalid "+resetPasswordDto.getOtp()+" OTP. Please enter a valid OTP.");
                    return new ResponseEntity<>(loginResponseDto, HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            loginResponseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(loginResponseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(loginResponseDto,HttpStatus.OK);
    }

    public LoginResponseDto setPassword(ResetPasswordDto resetPasswordDto, HttpServletRequest httpServletRequest, String emailAddress) {
        LoginResponseDto loginResponse = new LoginResponseDto();
        try {
            Member member = memberRepository.findByEmailAddress(emailAddress,false);
            Assert.isTrue(StringUtils.equals(resetPasswordDto.getPassword(), resetPasswordDto.getReCheckPassword()), PASSWORDS_DO_NOT_MATCH);
            if (ObjectUtils.isNotEmpty(member)) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
                boolean isPasswordMatch = passwordEncoder.matches(resetPasswordDto.getPassword(), member.getPassword());
                org.springframework.util.Assert.isTrue(!isPasswordMatch, OLD_PASSWORD_NEW_PASSWORD_ARE_SAME);
                final String encryptedPassword = applicationUtilities.getEncryptedPassword(resetPasswordDto.getPassword());
                member.setPassword(encryptedPassword);
                memberRepository.save(member);

                //login details
                Login login = new Login();
                login = loginService.saveLoginDetails(httpServletRequest, login, member);
                loginResponse.setMemberId(member.getId());
                loginResponse.setEmailAddress(member.getEmailAddress());
                loginResponse.setToken(login.getToken());
                loginResponse.setMessage(PASSWORD_CHANGED);
                logger.debug("User password has been updated successfully for {}", member.getEmailAddress());
            } else {
                throw new ApiException(MEMBER_NOT_FOUND);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return loginResponse;
    }
}
