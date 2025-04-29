package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CraftDto extends BaseResponseDto{
    private UUID id;
    private String name;
}
