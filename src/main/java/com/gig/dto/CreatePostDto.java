package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CreatePostDto {
    private String description;
    private String location;
    private Set<UUID> taggedMembers = new HashSet<>();
    private List<AttachmentsDto> attachments = new ArrayList<>();
}
