package com.gig.mappers;

import com.gig.dto.CreatePostDto;
import com.gig.dto.PostDto;
import com.gig.models.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "Spring")
public interface PostMapper {
    public static final PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Posts dtoToEntity(CreatePostDto createPostDto);

    PostDto entityToDto(Posts posts);

    List<PostDto> entityToDtoList(List<Posts> posts);
}
