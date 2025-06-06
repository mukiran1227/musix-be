package com.gig.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventSearchDTO {
    @Schema(nullable = true)
    private String location;
    
    @Schema(nullable = true)
    private List<String> categories;
    
    @Schema(nullable = true)
    private LocalDateTime startDate;
    
    @Schema(nullable = true)
    private LocalDateTime endDate;
    
    @Schema(nullable = true)
    private BigDecimal minPrice;
    
    @Schema(nullable = true)
    private BigDecimal maxPrice;
    
    @Schema(nullable = true)
    private String searchQuery;
}
