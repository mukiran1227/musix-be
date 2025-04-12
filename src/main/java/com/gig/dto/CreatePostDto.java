package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreatePostDto {
    private String description;
    private List<AttachmentsDto> attachments = new ArrayList<>();
}
