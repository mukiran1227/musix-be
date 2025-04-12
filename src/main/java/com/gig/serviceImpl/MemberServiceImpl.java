package com.gig.serviceImpl;

import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.config.EmailService;
import com.gig.dto.ChangePasswordDto;
import com.gig.dto.EmailDto;
import com.gig.dto.RegistrationDto;
import com.gig.exceptions.ApiException;
import com.gig.models.Member;
import com.gig.repository.MemberRepository;
import com.gig.service.MemberService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;

import static com.gig.applicationUtilities.ApplicationConstants.MEMBER_REGISTRATION_SUCCESSFULLY;
import static com.gig.applicationUtilities.ApplicationConstants.NEW_PASSWORD_AND_CONFIRM_NEW_PASSWORD_ARE_NOT_MATCHING;
import static com.gig.applicationUtilities.ApplicationConstants.OLD_PASSWORD_IS_WRONG;
import static com.gig.applicationUtilities.ApplicationConstants.OLD_PASSWORD_NEW_PASSWORD_ARE_SAME;
import static com.gig.applicationUtilities.ApplicationConstants.PASSWORDS_DO_NOT_MATCH;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private ApplicationUtilities applicationUtilities;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional(rollbackOn = {Exception.class, ApiException.class})
    public Member createAccount(RegistrationDto registrationDto) {
        Member member = new Member();
        try{
            Assert.isTrue(isPasswordMatch(registrationDto.getPassword(), registrationDto.getReCheckPassword()), PASSWORDS_DO_NOT_MATCH);
            final String encryptedPassword = applicationUtilities.getEncryptedPassword(registrationDto.getPassword());
            member.setPassword(encryptedPassword);
            member.setEmailAddress(registrationDto.getEmailAddress());
            memberRepository.save(member);
            Context context = new Context();
            EmailDto emailDto = new EmailDto();
            emailDto.setRecipient(member.getEmailAddress());
            emailDto.setSubject(MEMBER_REGISTRATION_SUCCESSFULLY);
            emailDto.setTemplateName("RegistrationEmailTemplate");
            emailService.sendMail(emailDto, context);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return member;
    }

    @Override
    public void updatePassword(ChangePasswordDto changePasswordDto, Member member) {
        try {
            Assert.isTrue(StringUtils.equals(changePasswordDto.getNewPassword(), changePasswordDto.getConfirmPassword()), NEW_PASSWORD_AND_CONFIRM_NEW_PASSWORD_ARE_NOT_MATCHING);
            Assert.isTrue(applicationUtilities.isPasswordMatched(changePasswordDto.getOldPassword(), member.getPassword()), OLD_PASSWORD_IS_WRONG);
            Assert.isTrue(!StringUtils.equals(changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword()), OLD_PASSWORD_NEW_PASSWORD_ARE_SAME);
            member.setPassword(applicationUtilities.getEncryptedPassword(changePasswordDto.getNewPassword()));
            memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getLocalizedMessage());
        }
    }

    public Boolean isPasswordMatch(String password, String reCheckPassword) {
        return StringUtils.equals(password, reCheckPassword);
    }
}
