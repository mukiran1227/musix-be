package com.gig.config;

import com.gig.mappers.EventMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    
    @Bean
    public EventMapper eventMapper() {
        return Mappers.getMapper(EventMapper.class);
    }
}
