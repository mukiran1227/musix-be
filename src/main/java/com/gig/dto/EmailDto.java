package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String recipient;
    private String subject;
    private String body;
    private String templateName;
}
