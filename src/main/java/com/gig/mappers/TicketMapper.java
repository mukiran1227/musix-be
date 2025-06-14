package com.gig.mappers;

import com.gig.dto.TicketDTO;
import com.gig.models.Tickets;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketDTO toDto(Tickets entity);
    
    Tickets toEntity(TicketDTO dto);
    
    List<TicketDTO> toDtoList(List<Tickets> entities);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTicketFromDto(TicketDTO dto, @MappingTarget Tickets entity);
}
