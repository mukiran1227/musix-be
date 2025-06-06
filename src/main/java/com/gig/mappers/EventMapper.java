package com.gig.mappers;

import com.gig.dto.EventDTO;
import com.gig.dto.TicketDTO;
import com.gig.dto.PerformerDTO;
import com.gig.models.Events;
import com.gig.models.Tickets;
import com.gig.models.Performers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "performers", ignore = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "coverImageUrl", source = "coverImageUrl")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "termsAndConditions", source = "termsAndConditions")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    Events toEntity(EventDTO dto);

    @Mapping(target = "tickets", source = "tickets")
    @Mapping(target = "performers", source = "performers")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "coverImageUrl", source = "coverImageUrl")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "termsAndConditions", source = "termsAndConditions")
    @Mapping(target = "creationTimestamp", source = "creationTimestamp")
    @Mapping(target = "createdBy", source = "createdBy")
    EventDTO toDto(Events entity);

    List<TicketDTO> toTicketDtoList(List<Tickets> entityList);
    List<TicketDTO> toTicketDtoList(Set<Tickets> entitySet);

    List<PerformerDTO> toPerformerDtoList(List<Performers> entityList);
    List<PerformerDTO> toPerformerDtoList(Set<Performers> entitySet);

    // Nested mappings
    TicketDTO toTicketDto(Tickets ticket);
    PerformerDTO toPerformerDto(Performers performer);

    // DTO to entity conversions
    Tickets ticketDtoToEntity(TicketDTO ticketDTO);
    Performers performerDtoToEntity(PerformerDTO performerDTO);
}

