package com.gig.mappers;

import com.gig.dto.AttachmentsDto;
import com.gig.models.Attachments;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AttachmentsMapper {
    AttachmentsMapper INSTANCE = Mappers.getMapper(AttachmentsMapper.class);

    AttachmentsDto toDto(Attachments entity);
    List<AttachmentsDto> toDtoList(List<Attachments> entities);
    List<AttachmentsDto> toDtoList(Set<Attachments> entities);

    Attachments toEntity(AttachmentsDto dto);
    List<Attachments> toEntityList(List<AttachmentsDto> dtos);
    Set<Attachments> toEntitySet(Set<AttachmentsDto> dtos);

    @AfterMapping
    default void afterToDto(Attachments entity, @MappingTarget AttachmentsDto dto) {
        dto.setDeleted(entity.getIsDeleted());
        dto.setCreationTimestamp(entity.getCreationTimestamp());
        dto.setUpdateTimestamp(entity.getUpdateTimestamp());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
    }

    @AfterMapping
    default void afterToEntity(AttachmentsDto dto, @MappingTarget Attachments entity) {
        entity.setIsDeleted(dto.getDeleted());
        entity.setCreationTimestamp(dto.getCreationTimestamp());
        entity.setUpdateTimestamp(dto.getUpdateTimestamp());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }
}
