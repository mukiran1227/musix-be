package com.gig.config;

import com.gig.mappers.EventMapper;
import com.gig.mappers.TicketMapper;
import com.gig.mappers.PerformerMapper;
import com.gig.mappers.AttachmentsMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {

    @Bean
    public EventMapper eventMapper() {
        return Mappers.getMapper(EventMapper.class);
    }

    @Bean
    public TicketMapper ticketMapper() {
        return Mappers.getMapper(TicketMapper.class);
    }

    @Bean
    public PerformerMapper performerMapper() {
        return Mappers.getMapper(PerformerMapper.class);
    }

    @Bean
    public AttachmentsMapper attachmentsMapper() {
        return Mappers.getMapper(AttachmentsMapper.class);
    }
}
