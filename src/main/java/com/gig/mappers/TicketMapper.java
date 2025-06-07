package com.gig.mappers;

import com.gig.dto.TicketDTO;
import com.gig.models.Tickets;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Tickets toEntity(TicketDTO dto);
    TicketDTO toDto(Tickets entity);

    List<TicketDTO> toDtoList(List<Tickets> entities);
    List<TicketDTO> toDtoList(Set<Tickets> entities);
}
