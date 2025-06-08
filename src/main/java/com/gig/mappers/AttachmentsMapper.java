package com.gig.mappers;

import com.gig.dto.AttachmentsDto;
import com.gig.models.Attachments;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AttachmentsMapper {
    AttachmentsMapper INSTANCE = Mappers.getMapper(AttachmentsMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uploaded", source = "uploaded")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "deleted", source = "isDeleted")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    AttachmentsDto toDto(Attachments entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uploaded", source = "uploaded")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "isDeleted", source = "deleted")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    Attachments toEntity(AttachmentsDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uploaded", source = "uploaded")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "deleted", source = "isDeleted")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    List<AttachmentsDto> toDtoList(List<Attachments> entities);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uploaded", source = "uploaded")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "deleted", source = "isDeleted")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    List<AttachmentsDto> toDtoList(Set<Attachments> entities);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uploaded", source = "uploaded")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "isDeleted", source = "deleted")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    List<Attachments> toEntityList(List<AttachmentsDto> dtos);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uploaded", source = "uploaded")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "isDeleted", source = "deleted")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "updateTimestamp", source = "updateTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
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
