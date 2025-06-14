package com.gig.mappers;

import com.gig.dto.TicketDTO;
import com.gig.models.Tickets;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    Tickets toEntity(TicketDTO dto);
    TicketDTO toDto(Tickets entity);

    List<TicketDTO> toDtoList(List<Tickets> entities);
}
