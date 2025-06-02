package com.gig.serviceImpl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.config.EmailService;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.ChangePasswordDto;
import com.gig.dto.EmailDto;
import com.gig.dto.RegistrationDto;
import com.gig.exceptions.ApiException;
import com.gig.models.Follow;
import com.gig.models.Member;
import com.gig.repository.FollowRepository;
import com.gig.repository.MemberRepository;
import com.gig.service.LoginService;
import com.gig.service.MemberService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private LoginService loginService;

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
            EmailDto emailDto = new EmailDto();
            emailDto.setSubject("Account Verification");
            emailDto.setTemplateName("AccountVerificationEmailTemplate");
            loginService.generateOtp(member.getEmailAddress(), null, member,emailDto);
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

    @Override
    public BaseResponseDto followOrUnfollow(String memberId, String followerId, Member loggedInMember) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Follow follow = followRepository.findByFollowerAndFollowed(followerId, memberId);
            if(ObjectUtils.isEmpty(follow)){
                Follow newFollower = new Follow();
                newFollower.setFollower(UUID.fromString(followerId));
                newFollower.setFollowed(UUID.fromString(memberId));
                newFollower.setCreatedBy(loggedInMember.getId());
                followRepository.save(newFollower);
                responseDto.setMessage("You have started following");
            }else if(BooleanUtils.isTrue(follow.getIsDeleted())){
                follow.setIsDeleted(false);
                follow.setUpdateTimestamp(LocalDateTime.now());
                follow.setUpdatedBy(loggedInMember.getId());
                followRepository.save(follow);
                responseDto.setMessage("You have started following");
            }
            else {
                follow.setIsDeleted(true);
                follow.setUpdateTimestamp(LocalDateTime.now());
                follow.setUpdatedBy(loggedInMember.getId());
                followRepository.save(follow);
                responseDto.setMessage("You have unfollowed ");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new ApiException(ex.getLocalizedMessage());
        }
        return responseDto;
    }

    @Override
    public BaseResponseDto removeFollower(String memberId, String loggedInMember) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Follow follow = followRepository.findByFollowerAndFollowed(memberId,loggedInMember);
            if(ObjectUtils.isNotEmpty(follow)){
                follow.setIsDeleted(true);
                followRepository.save(follow);
                responseDto.setMessage("You have removed the follower");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new ApiException(ex.getLocalizedMessage());
        }
        return responseDto;
    }

    public Boolean isPasswordMatch(String password, String reCheckPassword) {
        return StringUtils.equals(password, reCheckPassword);
    }
}
