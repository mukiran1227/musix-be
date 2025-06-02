package com.gig.controller;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.ChangePasswordDto;
import com.gig.dto.CraftDto;
import com.gig.dto.FollowersDto;
import com.gig.dto.MemberDto;
import com.gig.dto.RegistrationDto;
import com.gig.dto.RegistrationResponseDto;
import com.gig.dto.UpdateMemberDto;
import com.gig.facade.MemberFacade;
import com.gig.models.BaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/member")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private  MemberFacade memberFacade;


    @PostMapping("/create-account")
    public ResponseEntity<RegistrationResponseDto> createAccount(@RequestBody RegistrationDto registrationDto) {
        return memberFacade.createAccount(registrationDto);
    }

    @GetMapping("/getMember/{memberId}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable(value = "memberId") String memberId, HttpServletRequest request){
        return memberFacade.getMember(memberId,request);
    }

    @GetMapping("/getMember/token")
    public ResponseEntity<MemberDto> getMemberByToken(HttpServletRequest request){
        return memberFacade.getMember(null,request);
    }

    @PutMapping("/update-member")
    public ResponseEntity<BaseResponseDto> updateMember(@RequestBody UpdateMemberDto memberDto , HttpServletRequest request){
        return memberFacade.updateMember(memberDto,request);
    }

    @PostMapping("/update-password")
    public ResponseEntity<BaseResponseDto> updatePassword(@RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        return memberFacade.updatePassword(changePasswordDto, request);
    }

    @PostMapping("/followOrUnFollow")
    public ResponseEntity<BaseResponseDto> followOrUnfollow(@RequestParam(value = "memberId") String memberId ,@RequestParam(value = "followerId") String followerId, HttpServletRequest request){
        return memberFacade.followOrUnfollow(memberId,followerId,request);
    }

    @GetMapping("/fetchFollowerOrFollowingList")
    public ResponseEntity<List<FollowersDto>> fetchFollowerOrFollowingList(@RequestParam(value = "type") String type , HttpServletRequest request){
        return memberFacade.fetchFollowerOrFollowingList(type,request);
    }

    @GetMapping("/fetchAllCrafts")
    public ResponseEntity<List<CraftDto>> getAllCrafts(HttpServletRequest request){
        return memberFacade.getAllCrafts(request);
    }

    @PostMapping("/remove-follower")
    public ResponseEntity<BaseResponseDto> removeFollower(@RequestParam(value = "memberId") String memberId , HttpServletRequest request){
        return memberFacade.removeFollower(memberId,request);
    }

    @PostMapping("/delete-account")
    public ResponseEntity<BaseResponseDto> deleteAccount(@RequestParam(value = "email") String email , HttpServletRequest request){
        return memberFacade.deleteAccount(email,request);
    }
}
