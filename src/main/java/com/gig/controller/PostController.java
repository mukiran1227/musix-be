package com.gig.controller;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CommentsDto;
import com.gig.dto.CreatePostDto;
import com.gig.dto.PaginationDto;
import com.gig.dto.PostDto;
import com.gig.facade.PostFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member/post")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostFacade postFacade;

    @PostMapping("/create-post")
    public ResponseEntity<BaseResponseDto> createPost(@RequestBody CreatePostDto createPostDto , HttpServletRequest request){
        return postFacade.createPost(createPostDto,request);
    }

    @GetMapping("/get-post/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(value = "postId") String postId , HttpServletRequest request){
        return postFacade.getPostById(postId,request);
    }

    @PutMapping("/update-post")
    public ResponseEntity<BaseResponseDto> updatePost(@RequestParam(value = "postId") String postId,@RequestBody CreatePostDto createPostDto, HttpServletRequest request){
        return postFacade.updatePost(postId,createPostDto,request);
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<BaseResponseDto> deletePost(@RequestParam(value = "postId") String postId , HttpServletRequest request){
        return postFacade.deletePost(postId,request);
    }

    @GetMapping("/like-post")
    public ResponseEntity<BaseResponseDto> likePost(@RequestParam(value = "postId") String postId, HttpServletRequest request){
        return postFacade.likePost(postId,request);
    }

    @PostMapping("/add-comment")
    public ResponseEntity<BaseResponseDto> addComment(@RequestParam(value = "postId")String postId, @RequestBody CommentsDto commentsDto , HttpServletRequest request){
        return postFacade.addComment(postId,commentsDto,request);
    }

    @GetMapping("/get-all/post")
    public ResponseEntity<PaginationDto> getAllPosts(@RequestParam(value = "limit") int limit, @RequestParam(value = "offset") int offset,HttpServletRequest request){
        return postFacade.getAllPosts(limit,offset,request);
    }

    @GetMapping("/get-loggedIn-user-posts")
    public ResponseEntity<PaginationDto> getLoggedInUserPosts(@RequestParam(value = "limit") int limit, @RequestParam(value = "offset") int offset, HttpServletRequest request){
        return postFacade.getLoggedInUserPosts(limit,offset,request);
    }
}
