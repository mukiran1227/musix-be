package com.gig.service;

import com.gig.dto.CollaborationDto;
import com.gig.models.Member;

import java.util.List;

public interface CollaborationService {
    List<CollaborationDto> getUserCollaborations(Member member);
}
