package com.gig.facadeImpl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.CommentsDto;
import com.gig.dto.CreatePostDto;
import com.gig.dto.PaginationDto;
import com.gig.dto.PostDto;
import com.gig.facade.PostFacade;
import com.gig.models.Member;
import com.gig.models.Posts;
import com.gig.repository.PostsRepository;
import com.gig.service.PostService;
import com.gig.serviceImpl.PostsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gig.applicationUtilities.ApplicationConstants.NO_ACCESS_TO_DELETE;
import static com.gig.applicationUtilities.ApplicationConstants.NO_ACCESS_TO_EDIT;
import static com.gig.applicationUtilities.ApplicationConstants.POST_DELETED;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Component
public class PostFacadeImpl implements PostFacade {

    @Autowired
    private ApplicationUtilities applicationUtilities;

    @Autowired
    private PostService postService;

    @Autowired
    private PostsServiceImpl postsServiceImpl;

    @Autowired
    private PostsRepository postsRepository;

    @Override
    public ResponseEntity<BaseResponseDto> createPost(CreatePostDto createPostDto, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member member = applicationUtilities.getLoggedInUser(request);
            responseDto = postService.createPost(createPostDto,member);
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PostDto> getPostById(String postId, HttpServletRequest request) {
        PostDto postDto = new PostDto();
        try{
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            postDto = postService.getPost(postId,loggedInMember);
        }catch (Exception ex){
            ex.printStackTrace();
            postDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(postDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> updatePost(String postId, CreatePostDto createPostDto, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member member = applicationUtilities.getLoggedInUser(request);
            Posts posts = postsRepository.findByPostId(postId,false);
            if(ObjectUtils.isNotEmpty(posts) && !ObjectUtils.notEqual(posts.getMember(),member)){
                responseDto = postService.updatePost(posts,member,createPostDto);
            }else {
                responseDto.setMessage(NO_ACCESS_TO_EDIT);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> deletePost(String postId, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member member = applicationUtilities.getLoggedInUser(request);
            Posts posts = postsRepository.findByPostId(postId,false);
            if(ObjectUtils.isNotEmpty(posts) && !ObjectUtils.notEqual(posts.getMember(),member)){
                posts.setIsDeleted(true);
                postsRepository.save(posts);
                responseDto.setMessage(POST_DELETED);
            }else {
                responseDto.setMessage(NO_ACCESS_TO_DELETE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> likePost(String postId, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member member = applicationUtilities.getLoggedInUser(request);
            responseDto = postService.likePost(postId,member);
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponseDto> addComment(String postId, CommentsDto commentsDto, HttpServletRequest request) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Member member = applicationUtilities.getLoggedInUser(request);
            responseDto = postService.addComment(postId,member,commentsDto);
        }catch (Exception ex){
            ex.printStackTrace();
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginationDto> getAllPosts(int limit, int offset, HttpServletRequest request) {
        PaginationDto paginationDto = new PaginationDto();
        try {
            Member loggedInMember = applicationUtilities.getLoggedInUser(request);
            List<Posts> posts = postsRepository.findAllPosts(limit,offset);
            List<PostDto> postDtoList = emptyIfNull(posts).stream()
                        .map(post -> postsServiceImpl.setPostDetails(loggedInMember, post))
                        .toList();
            if(CollectionUtils.isNotEmpty(postDtoList)){
                paginationDto.setPostDto(postDtoList);
                paginationDto.setPerPageCount(postDtoList.size());
                paginationDto.setTotalCount(postsRepository.findAllPostsCount());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(paginationDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginationDto> getLoggedInUserPosts(int limit, int offset, HttpServletRequest request) {
        PaginationDto paginationDto = new PaginationDto();
        try{
           Member loggedInMember = applicationUtilities.getLoggedInUser(request);
           List<Posts> posts = postsRepository.findByMemberId(loggedInMember.getId().toString(),limit,offset);
            List<PostDto> postDtoList = emptyIfNull(posts).stream()
                    .map(post -> postsServiceImpl.setPostDetails(loggedInMember, post))
                    .toList();
            if(CollectionUtils.isNotEmpty(postDtoList)){
                paginationDto.setPostDto(postDtoList);
                paginationDto.setPerPageCount(postDtoList.size());
                paginationDto.setTotalCount(postsRepository.findByMemberIdCount(loggedInMember.getId().toString()));
            }
        }catch (Exception ex){
            ex.printStackTrace();
            paginationDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(paginationDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(paginationDto,HttpStatus.OK);
    }

}
