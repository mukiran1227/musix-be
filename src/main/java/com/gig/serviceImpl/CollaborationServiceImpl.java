/*
package com.gig.serviceImpl;

import com.gig.dto.CollaborationDto;
import com.gig.exceptions.ApiException;
import com.gig.mappers.CollaborationMapper;
import com.gig.models.Collaboration;
import com.gig.models.Member;
import com.gig.repository.CollaborationRepository;
import com.gig.service.CollaborationService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollaborationServiceImpl implements CollaborationService {

    @Autowired
    private CollaborationRepository collaborationRepository;

    @Override
    public List<CollaborationDto> getUserCollaborations(Member member) {
        List<CollaborationDto> collaborationDto = new ArrayList<>();
        try{
            List<Collaboration> collaborations = collaborationRepository.findByMemberId(member.getId().toString());
            collaborationDto = CollaborationMapper.INSTANCE.listEntityToDto(collaborations);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new ApiException(ex.getMessage());
        }
        return collaborationDto;
    }
}
*/
