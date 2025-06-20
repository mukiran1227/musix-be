package com.gig.mappers;

import com.gig.dto.*;
import com.gig.models.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.transaction.annotation.Transactional
@org.mapstruct.Mapper(uses = {TicketMapper.class, PerformerMapper.class, AttachmentsMapper.class})
public interface EventMapper extends com.gig.config.MapperConfig {


    @Named("toSimpleEventDTO")
    @Mapping(target = "minPrice", expression = "java(events.getTickets().stream().mapToDouble(t -> t.getPrice()).min().orElse(0.0))")
    @Mapping(target = "maxPrice", expression = "java(events.getTickets().stream().mapToDouble(t -> t.getPrice()).max().orElse(0.0))")
    SimpleEventDTO  toSimpleEventDTO(Events events);

    @Mapping(target = "minPrice", expression = "java(eventDTO.getTickets().stream().mapToDouble(t -> t.getPrice()).min().orElse(0.0))")
    @Mapping(target = "maxPrice", expression = "java(eventDTO.getTickets().stream().mapToDouble(t -> t.getPrice()).max().orElse(0.0))")
    SimpleEventDTO toSimpleEventDTO(EventDTO eventDTO);

    default List<SimpleEventDTO> toSimpleEventDTOList(List<Events> eventsList) {
        if (eventsList == null) {
            return List.of();
        }
        return eventsList.stream()
            .map(this::toSimpleEventDTO)
            .collect(Collectors.toList());
    }

    Events toEntity(EventDTO eventDTO);

    @Named("toDto")
    @Mapping(target = "tickets", source = "tickets", qualifiedByName = "toTicketDtoList")
    @Mapping(target = "performers", source = "performers", qualifiedByName = "toPerformerDtoList")
    @Mapping(target = "coverImageUrl", source = "coverImageUrl", qualifiedByName = "mapAttachmentsWithDeletedFlag")
    @Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "mapAttachmentsWithDeletedFlag")
    @Mapping(target = "bookmarked", expression = "java(loggedInMember != null && entity.getBookmarkedBy().stream().anyMatch(m -> m.getId().equals(loggedInMember.getId())))")
    EventDTO toDto(Events entity, @Context Member loggedInMember);

    @AfterMapping
    @Named("afterToDto")
    default void afterToDto(Events entity, @MappingTarget EventDTO dto, @Context Member loggedInMember) {
        if (entity.getMember() != null) {
            dto.setCreatedBy(entity.getMember().getFirstName() + " " + entity.getMember().getLastName());
        }
    }

    @Named("toDtoList")
    default List<EventDTO> toDtoList(List<Events> entities, @Context Member loggedInMember) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
            .map(event -> toDto(event, loggedInMember))
            .collect(Collectors.toList());
    }

    @Named("toDtoListSet")
    default List<EventDTO> toDtoList(Set<Events> entities, @Context Member loggedInMember) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
            .map(event -> toDto(event, loggedInMember))
            .collect(Collectors.toList());
    }

    @Named("toTicketDtoList")
    default List<TicketDTO> toTicketDtoList(List<Tickets> tickets) {
        if (tickets == null) {
            return null;
        }
        return tickets.stream()
            .map(ticket -> {
                TicketDTO dto = new TicketDTO();
                dto.setId(ticket.getId());
                dto.setName(ticket.getName());
                dto.setDescription(ticket.getDescription());
                dto.setPrice(ticket.getPrice());
                // Quantity is not a field in Tickets entity
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Named("mapAttachmentsWithDeletedFlag")
    default List<AttachmentsDto> mapAttachmentsWithDeletedFlag(List<Attachments> attachments) {
        if (attachments == null) {
            return null;
        }
        
        return attachments.stream()
            .map(attachment -> {
                AttachmentsDto dto = new AttachmentsDto();
                dto.setId(attachment.getId());
                dto.setFileName(attachment.getFileName());
                dto.setContentType(attachment.getContentType());
                dto.setUploadUrl(attachment.getUploadUrl());
                dto.setUploaded(attachment.getUploaded());
                dto.setDeleted(attachment.getIsDeleted() != null && attachment.getIsDeleted());
                dto.setCreationTimestamp(attachment.getCreationTimestamp());
                dto.setUpdateTimestamp(attachment.getUpdateTimestamp());
                dto.setCreatedBy(attachment.getCreatedBy());
                dto.setUpdatedBy(attachment.getUpdatedBy());
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Named("toPerformerDtoList")
    default List<PerformerDTO> toPerformerDtoList(List<Performers> performers) {
        if (performers == null) {
            return null;
        }
        return performers.stream()
            .map(performer -> {
                PerformerDTO dto = new PerformerDTO();
                dto.setId(performer.getId());
                dto.setName(performer.getName());
                dto.setRole(performer.getRole());
                dto.setImageUrl(performer.getImageUrl());
                dto.setCreationTimestamp(performer.getCreationTimestamp());
                dto.setUpdateTimestamp(performer.getUpdateTimestamp());
                dto.setCreatedBy(performer.getCreatedBy());
                dto.setUpdatedBy(performer.getUpdatedBy());
                return dto;
            })
            .collect(Collectors.toList());
    }
}
