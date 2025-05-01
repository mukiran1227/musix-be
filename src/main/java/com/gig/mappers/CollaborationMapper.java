package com.gig.mappers;

import com.gig.dto.CollaborationDto;
import com.gig.dto.CreateCollaborationDto;
import com.gig.models.Collaboration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "Spring")
public interface CollaborationMapper {

    public static final CollaborationMapper INSTANCE = Mappers.getMapper(CollaborationMapper.class);

    Collaboration dtoToEntity(CreateCollaborationDto createCollaborationDto);

    CollaborationDto entityToDto(Collaboration collaboration);

    List<CollaborationDto> listEntityToDto(List<Collaboration> collaborations);
}
