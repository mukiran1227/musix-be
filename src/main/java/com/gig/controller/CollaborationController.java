/*
package com.gig.controller;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CollaborationDto;
import com.gig.dto.CreateCollaborationDto;
import com.gig.facade.CollaborationFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class CollaborationController {

    @Autowired
    private CollaborationFacade collaborationFacade;


    @PostMapping("/create/collaboration")
    public ResponseEntity<BaseResponseDto> createCollaboration(@RequestBody CreateCollaborationDto createCollaborationDto , HttpServletRequest request){
        return collaborationFacade.createCollaboration(createCollaborationDto,request);
    }

    @GetMapping("/get/collaboration")
    public ResponseEntity<CollaborationDto> getCollaboration(@RequestParam(value = "collaborationId") String collaborationId , HttpServletRequest request){
        return collaborationFacade.getCollaboration(collaborationId,request);
    }

    @GetMapping("/get-userCollaborations")
    public ResponseEntity<List<CollaborationDto>> getUserCollaborations(HttpServletRequest request){
        return collaborationFacade.getUserCollaborations(request);
    }
}
*/
