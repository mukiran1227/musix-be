package com.gig.service;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CommentsDto;
import com.gig.dto.CreatePostDto;
import com.gig.dto.PostDto;
import com.gig.models.Member;
import com.gig.models.Posts;

public interface PostService {
    PostDto createPost(CreatePostDto createPostDto, Member member);

    PostDto getPost(String postId, Member loggedInMember);

    BaseResponseDto updatePost(Posts posts, Member member,CreatePostDto createPostDto);

    BaseResponseDto likePost(String postId, Member member);

    BaseResponseDto addComment(String postId, Member member, CommentsDto commentsDto);
}
