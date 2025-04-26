package com.gig.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationDto extends  BaseResponseDto{
    private int totalCount;
    private int perPageCount;
    private List<PostDto> postDto;
    private List<CommentsDto> commentsDto;
}
