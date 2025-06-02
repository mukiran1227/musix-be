/*
package com.gig.facadeImpl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.CollaborationDto;
import com.gig.dto.CreateCollaborationDto;
import com.gig.facade.CollaborationFacade;
import com.gig.mappers.CollaborationMapper;
import com.gig.models.Collaboration;
import com.gig.models.Member;
import com.gig.repository.CollaborationRepository;
import com.gig.service.CollaborationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.gig.applicationUtilities.ApplicationConstants.BRAND_PAGE_IS_CREATED;


@Component
@RequiredArgsConstructor
public class CollaborationFacadeImpl implements CollaborationFacade {
    @Autowired
    private ApplicationUtilities applicationUtilities;

    @Autowired
    private CollaborationRepository collaborationRepository;

    @Autowired
    private CollaborationService collaborationService;

    @Override
    public ResponseEntity<BaseResponseDto> createCollaboration(CreateCollaborationDto createCollaborationDto, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try {
            Member member =  applicationUtilities.getLoggedInUser(request);
            Collaboration collaboration = CollaborationMapper.INSTANCE.dtoToEntity(createCollaborationDto);
            collaboration.setCreatedBy(member.getId());
            collaborationRepository.save(collaboration);
            responseDto.setMessage(BRAND_PAGE_IS_CREATED);
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollaborationDto> getCollaboration(String collaborationId, HttpServletRequest request) {
        CollaborationDto collaborationDto = new CollaborationDto();
        try{
           applicationUtilities.getLoggedInUser(request);
           Collaboration collaboration = collaborationRepository.findByIdAndIsDeleted(collaborationId);
           collaborationDto = CollaborationMapper.INSTANCE.entityToDto(collaboration);
        }catch (Exception ex){
            ex.printStackTrace();
            collaborationDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(collaborationDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(collaborationDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CollaborationDto>> getUserCollaborations(HttpServletRequest request) {
        List<CollaborationDto> collaborationDto = new ArrayList<>();
        try{
          Member member = applicationUtilities.getLoggedInUser(request);
          collaborationDto = collaborationService.getUserCollaborations(member);
        }catch (Exception ex){
            ex.printStackTrace();
            CollaborationDto collaboration = new CollaborationDto();
            collaboration.setMessage(ex.getMessage());
            collaborationDto.add(collaboration);
            return new ResponseEntity<>(collaborationDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(collaborationDto,HttpStatus.OK);
    }
}
*/
