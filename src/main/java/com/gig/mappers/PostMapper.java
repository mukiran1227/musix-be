package com.gig.mappers;

import com.gig.dto.PostDto;
import com.gig.dto.AttachmentsDto;
import com.gig.dto.TaggedMemberDto;
import com.gig.models.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


    PostDto entityToDto(Posts posts);
}
