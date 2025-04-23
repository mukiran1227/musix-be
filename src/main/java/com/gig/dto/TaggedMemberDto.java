package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class TaggedMemberDto {
    private UUID id;
    private String name;
    private String imageUrl;
}
