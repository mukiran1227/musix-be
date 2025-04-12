package com.gig.serviceImpl;

import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.config.JwtTokenUtils;
import com.gig.config.OTPService;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.LoginDto;
import com.gig.dto.LoginResponseDto;
import com.gig.dto.ResetPasswordDto;
import com.gig.dto.VerifyRequestDto;
import com.gig.enums.LoginStatusEnum;
import com.gig.exceptions.ApiException;
import com.gig.facadeImpl.LoginFacadeImpl;
import com.gig.models.Login;
import com.gig.models.Member;
import com.gig.repository.LoginRepository;
import com.gig.repository.MemberRepository;
import com.gig.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.gig.applicationUtilities.ApplicationConstants.INVALID_CREDENTIALS;
import static com.gig.applicationUtilities.ApplicationConstants.MAIL_SENT;
import static com.gig.applicationUtilities.ApplicationConstants.USER_LOGGED_OUT_SUCCESSFULLY;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private  LoginRepository loginRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ApplicationUtilities applicationUtilities;

    @Autowired
    private OTPService otpService;

    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    public Login findByTokenAndStatus(String authToken, LoginStatusEnum loginStatusEnum) {
        return loginRepository.findByTokenAndStatus(authToken, loginStatusEnum.toString());
    }

    @Override
    public Login saveLoginDetails(HttpServletRequest request, Login login, Member member) {
        if(ObjectUtils.isNotEmpty(request)) {
            login.setBrowserDetails(request.getHeader("User-Agent"));
        }
        login.setStatus(LoginStatusEnum.LOGGED_IN);
        login.setMember(member);
        login.setToken(jwtTokenUtils.generateToken(member, request));
        login.setLoggedInTime(LocalDateTime.now());
        loginRepository.save(login);
        return login;
    }

    @Override
    public ResponseEntity<BaseResponseDto> logout(HttpServletRequest request) {
        BaseResponseDto baseResponseDTO = new BaseResponseDto();
        try {
            Login login = applicationUtilities.getLogin(request);
            login.setLoggedOutTime(LocalDateTime.now());
            login.setStatus(LoginStatusEnum.LOGGED_OUT);
            Member member = login.getMember();
            memberRepository.save(member);
            loginRepository.save(login);
            baseResponseDTO.setMessage(USER_LOGGED_OUT_SUCCESSFULLY);
        }catch (Exception ex){
            ex.printStackTrace();
            baseResponseDTO.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @Override
    public Login login(LoginDto loginDTO, HttpServletRequest httpServletRequest, LoginResponseDto loginResponseDto) throws ApiException {
        Login login = new Login();
        try{
            final Member member = memberRepository.findByEmailAddress(loginDTO.getEmailAddress(), false);
            Assert.notNull(member, ApplicationConstants.LOGIN_NOT_FOUND);
            boolean isPasswordMatch = applicationUtilities.isPasswordMatched(loginDTO.getPassword(), member.getPassword());
            Assert.isTrue(isPasswordMatch, INVALID_CREDENTIALS);
            loginResponseDto.setIsVerified(member.getIsVerified());
            saveLoginDetails(httpServletRequest, login, member);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return login;
    }

    @Override
    public BaseResponseDto generateOtp(String username, BaseResponseDto baseResponseDto, Member member) {
        try {
            logger.info("LoginRegistrationFacadeImpl::generateAndSaveOtp :: Entering generate OTP");
            Map<String, Object> resource = otpService.generateOtp(member.getEmailAddress().toLowerCase());
            if (resource.get(ApplicationConstants.IS_MAIL_SENT).equals(true)) {
                baseResponseDto.setMessage(ApplicationConstants.VERIFICATION_MAIL);
            }
            Integer otp = (Integer) resource.get("otp");
            member.setOtp(otp);
            memberRepository.save(member);
            baseResponseDto.setMessage(MAIL_SENT);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return baseResponseDto;
    }

    @Override
    public Map<String, Object> verifyOtp(Member member, ResetPasswordDto resetPasswordDto) {
        Map<String, Object> resource = new HashMap<>();
        try {
            resource = otpService.validateOTP(member.getEmailAddress().toLowerCase(), resetPasswordDto.getOtp());
        } catch(Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getLocalizedMessage());
        }
        return resource;
    }
}
