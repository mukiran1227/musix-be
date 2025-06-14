package com.gig.mappers;

import com.gig.dto.PerformerDTO;
import com.gig.models.Performers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PerformerMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    Performers toEntity(PerformerDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    PerformerDTO toDto(Performers entity);

    List<PerformerDTO> toDtoList(List<Performers> entities);

    List<PerformerDTO> toDtoList(Set<Performers> entities);
}

