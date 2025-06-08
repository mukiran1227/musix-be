package com.gig.mappers;

import com.gig.dto.AttachmentsDto;
import com.gig.dto.EventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.models.Attachments;
import com.gig.models.Events;
import com.gig.models.Tickets;
import com.gig.models.Performers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TicketMapper.class, PerformerMapper.class})
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Named("toEntity")
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "performers", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "creationTimestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateTimestamp", expression = "java(java.time.LocalDateTime.now())")
    Events toEntity(EventDTO dto);

    @AfterMapping
    default void mapTickets(EventDTO dto, @MappingTarget Events entity) {
        if (dto.getTickets() != null) {
            entity.setTickets(dto.getTickets().stream()
                .map(TicketMapper.INSTANCE::toEntity)
                .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void mapPerformers(EventDTO dto, @MappingTarget Events entity) {
        if (dto.getPerformers() != null) {
            entity.setPerformers(dto.getPerformers().stream()
                .map(PerformerMapper.INSTANCE::toEntity)
                .collect(Collectors.toSet()));
        }
    }

    @Named("toDto")
    @Mapping(target = "tickets", expression = "java(TicketMapper.INSTANCE.toDtoList(entity.getTickets()))")
    @Mapping(target = "performers", expression = "java(PerformerMapper.INSTANCE.toDtoList(entity.getPerformers()))")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "coverImageUrl", source = "coverImageUrl", qualifiedByName = "mapAttachmentsWithDeletedFlag")
    @Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "mapAttachmentsWithDeletedFlag")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "termsAndConditions", source = "termsAndConditions")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    EventDTO toDto(Events entity);

    @AfterMapping
    @Named("afterToDto")
    default void afterToDto(Events entity, @MappingTarget EventDTO dto) {
        if (entity.getMember() != null) {
            dto.setCreatedBy(entity.getMember().getFirstName() + " " + entity.getMember().getLastName());
        }
    }

    @Named("toDtoList")
    List<EventDTO> toDtoList(List<Events> entities);

    @Named("toDtoListSet")
    List<EventDTO> toDtoList(Set<Events> entities);

    @Named("toTicketDtoList")
    default List<TicketDTO> toTicketDtoList(Set<Tickets> tickets) {
        return TicketMapper.INSTANCE.toDtoList(tickets);
    }

    @Named("mapAttachmentsWithDeletedFlag")
    default List<AttachmentsDto> mapAttachmentsWithDeletedFlag(List<Attachments> attachments) {
        return AttachmentsMapper.INSTANCE.toDtoList(attachments).stream()
                .map(dto -> {
                    dto.setDeleted(
                            attachments.stream()
                                    .filter(attachment -> attachment.getId().equals(dto.getId()))
                                    .findFirst()
                                    .map(Attachments::getIsDeleted)
                                    .orElse(false)
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Named("toPerformerDtoList")
    default List<PerformerDTO> toPerformerDtoList(Set<Performers> performers) {
        return PerformerMapper.INSTANCE.toDtoList(performers);
    }
}
