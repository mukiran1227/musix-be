package com.gig.facade.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.ChangePasswordDto;
import com.gig.dto.CraftDto;
import com.gig.dto.FollowersDto;
import com.gig.dto.MemberDto;
import com.gig.dto.RegistrationDto;
import com.gig.dto.RegistrationResponseDto;
import com.gig.dto.UpdateMemberDto;
import com.gig.exceptions.ApiException;
import com.gig.facade.MemberFacade;
import com.gig.mappers.MemberMapper;
import com.gig.models.Craft;
import com.gig.models.Login;
import com.gig.models.Member;
import com.gig.repository.CraftRepository;
import com.gig.repository.FollowRepository;
import com.gig.repository.MemberRepository;
import com.gig.repository.PostsRepository;
import com.gig.service.LoginService;
import com.gig.service.MemberService;
import com.gig.service.impl.LoginServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.gig.applicationUtilities.ApplicationConstants.FOLLOWER;
import static com.gig.applicationUtilities.ApplicationConstants.FOLLOWING;
import static com.gig.applicationUtilities.ApplicationConstants.MEMBERSHIP_REGISTERED_SUCCESSFULLY;
import static com.gig.applicationUtilities.ApplicationConstants.MEMBER_NOT_FOUND;
import static com.gig.applicationUtilities.ApplicationConstants.PASSWORD_UPDATED_SUCCESSFULLY;
import static com.gig.applicationUtilities.ApplicationConstants.USER_ALREADY_EXIST;

@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {

    @Autowired
    private  MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ApplicationUtilities applicationUtilities;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CraftRepository craftRepository;

    @Autowired
    private LoginServiceImpl loginServiceImpl;

    @Autowired
    private PostsRepository postsRepository;

    /*@Autowired
    private CollaborationRepository collaborationRepository;
*/
    private final Logger logger = LoggerFactory.getLogger(MemberFacadeImpl.class);

    @Override
    public ResponseEntity<RegistrationResponseDto> createAccount(RegistrationDto registrationDto) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        try{
            Member isMemberExist =memberRepository.findByEmailAddress(registrationDto.getEmailAddress(),false);
            if(ObjectUtils.isEmpty(isMemberExist)){
              Member member = memberService.createAccount(registrationDto);
              if(ObjectUtils.isNotEmpty(member)) {
                  Login login = new Login();
                  login = loginService.saveLoginDetails(null, login, member);
                  responseDto = MemberMapper.INSTANCE.memberLoginEntityToDto(member);
                  responseDto.setToken(login.getToken());
                  responseDto.setMessage(MEMBERSHIP_REGISTERED_SUCCESSFULLY);
              }
            }else {
                logger.debug("MemberFacadeImpl::createAccount: {}", USER_ALREADY_EXIST);
                throw new ApiException(USER_ALREADY_EXIST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MemberDto> getMember(String memberId, HttpServletRequest request) {
        MemberDto memberDto = new MemberDto();
        try{
            Member member ;
            member = applicationUtilities.getLoggedInUser(request);
            if(StringUtils.isNotEmpty(memberId)){
                member = memberRepository.findByIdAndIsDeleted(memberId,false);
                if(ObjectUtils.isNotEmpty(member)){
                    memberDto = MemberMapper.INSTANCE.memberEntityToDto(member);
                }
            }else {
                memberDto = MemberMapper.INSTANCE.memberEntityToDto(member);
            }
            if(ObjectUtils.isNotEmpty(memberDto)){
                // Get followers count
                int followerCount = followRepository.fetchFollowersCount(member.getId().toString());
                memberDto.setFollowersCount(followerCount);

                // Get following count
                int followingCount = followRepository.fetchFollowingCount(member.getId().toString());
                memberDto.setFollowingCount(followingCount);

                // Get post count
                int postCount = postsRepository.countByMemberIdAndIsDeletedFalse(memberId);
                memberDto.setPostCount(postCount);

                /*if(ObjectUtils.isNotEmpty(memberDto.getCraft())){
                    Craft craft = craftRepository.findByIdAndIsDeleted(memberDto.getCraft());
                    if(ObjectUtils.isNotEmpty(craft)) {
                        memberDto.setCraftId(craft.getId().toString());
                        memberDto.setCraft(craft.getName());
                    }
                }*/
                /*List<Collaboration> collaborations = collaborationRepository.findByMemberId(memberDto.getId().toString());
                if(CollectionUtils.isNotEmpty(collaborations)){
                    List<CollaborationDto> collaborationDtoList = new ArrayList<>();
                    collaborations.forEach(collaboration -> {
                        CollaborationDto collaborationDto = new CollaborationDto();
                        collaborationDto.setId(collaboration.getId());
                        collaborationDto.setName(collaboration.getName());
                        collaborationDtoList.add(collaborationDto);
                    });
                    memberDto.setCollaborationDto(collaborationDtoList);
                }*/
            }
        }catch (Exception ex){
            ex.printStackTrace();
            memberDto.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(memberDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(memberDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> updateMember(UpdateMemberDto memberDto, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member member = memberRepository.findByIdAndIsDeleted(memberDto.getId().toString(),false);
            if(ObjectUtils.isNotEmpty(member)){
                member = objectMapper.readerForUpdating(member).readValue(objectMapper.writeValueAsBytes(memberDto));
                memberRepository.save(member);
                responseDto.setMessage(ApplicationConstants.MEMBER_DETAILS_UPDATED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> updatePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            Member member = applicationUtilities.getLoggedInUser(request);
            Assert.notNull(member, MEMBER_NOT_FOUND);
            memberService.updatePassword(changePasswordDto, member);
            baseResponseDto.setMessage(PASSWORD_UPDATED_SUCCESSFULLY);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getLocalizedMessage());
        }
        return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> followOrUnfollow(String memberId, String followerId, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member loggedInMember  = applicationUtilities.getLoggedInUser(request);
            responseDto = memberService.followOrUnfollow(memberId,followerId,loggedInMember);
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<FollowersDto>> fetchFollowerOrFollowingList(String type, HttpServletRequest request) {
        List<FollowersDto> followersDto = new ArrayList<>();
        try{
            Member member = applicationUtilities.getLoggedInUser(request);
            if(FOLLOWER.equalsIgnoreCase(type)) {
                followersDto = followRepository.fetchFollwerList(member.getId().toString());
            }else if(FOLLOWING.equalsIgnoreCase(type)) {
                followersDto = followRepository.fetchFollowingList(member.getId().toString());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(followersDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(followersDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CraftDto>> getAllCrafts(HttpServletRequest request) {
        List<CraftDto> craftDtoList = new ArrayList<>();
        try{
            List<Craft> craftList = craftRepository.fetchAllCrafts();
            craftDtoList = MemberMapper.INSTANCE.craftEntityToDto(craftList);
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(craftDtoList,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(craftDtoList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> removeFollower(String memberId, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            responseDto = memberService.removeFollower(memberId,loggedInMember.getId().toString());
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> deleteAccount(String email, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member loggedInMember  = applicationUtilities.getLoggedInUser(request);
            Member member = memberRepository.findByEmailAddress(email,false);
            if(ObjectUtils.isNotEmpty(member) && Objects.equals(loggedInMember,member)){
                member.setIsDeleted(true);
                memberRepository.save(member);
                loginServiceImpl.logout(request);
                responseDto.setMessage("Account Deleted Successfully");
            }else {
                responseDto.setMessage("You don't have access to delete account");
            }

        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}
