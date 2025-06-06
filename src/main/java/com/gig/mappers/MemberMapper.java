package com.gig.mappers;

import com.gig.dto.CraftDto;
import com.gig.dto.EventDTO;
import com.gig.dto.MemberDto;
import com.gig.dto.PerformerDTO;
import com.gig.dto.RegistrationResponseDto;
import com.gig.dto.TicketDTO;
import com.gig.models.Craft;
import com.gig.models.Events;
import com.gig.models.Member;
import com.gig.models.Performers;
import com.gig.models.Tickets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "Spring")
public interface MemberMapper {
    public static final MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    RegistrationResponseDto memberLoginEntityToDto(Member member);

    MemberDto memberEntityToDto(Member member);

    List<CraftDto> craftEntityToDto(List<Craft> craftList);
}
