package com.gig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationDto extends  BaseResponseDto{
    private int totalCount;
    private int perPageCount;
    private List<PostDto> postDto;
}
