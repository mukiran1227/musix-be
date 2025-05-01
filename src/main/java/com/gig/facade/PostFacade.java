package com.gig.facade;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CommentsDto;
import com.gig.dto.CreatePostDto;
import com.gig.dto.PaginationDto;
import com.gig.dto.PostDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PostFacade {
    ResponseEntity<PostDto> createPost(CreatePostDto createPostDto, HttpServletRequest request);

    ResponseEntity<PostDto> getPostById(String postId, HttpServletRequest request);

    ResponseEntity<BaseResponseDto> updatePost(String postId,CreatePostDto createPostDto, HttpServletRequest request);

    ResponseEntity<BaseResponseDto> deletePost(String postId, HttpServletRequest request);

    ResponseEntity<BaseResponseDto> likePost(String postId, HttpServletRequest request);

    ResponseEntity<BaseResponseDto> addComment(String postId, CommentsDto commentsDto, HttpServletRequest request);

    ResponseEntity<PaginationDto> getAllPosts(int limit, int offset,HttpServletRequest request);

    ResponseEntity<PaginationDto> getLoggedInUserPosts(int limit, int offset,HttpServletRequest request);

    ResponseEntity<PaginationDto> getCommentsByPostId(String postId, int limit, int offset, HttpServletRequest request);
}
