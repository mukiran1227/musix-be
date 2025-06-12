package com.gig.facade.impl;

import com.gig.applicationUtilities.ApplicationUtilities;
import com.gig.dto.BaseResponseDto;
import com.gig.dto.CommentsDto;
import com.gig.dto.CreatePostDto;
import com.gig.dto.LikeResponseDTO;
import com.gig.dto.PageResponseDTO;
import com.gig.dto.PaginationDto;
import com.gig.dto.PostDto;
import com.gig.facade.PostFacade;
import com.gig.models.Comments;
import com.gig.models.Member;
import com.gig.models.Posts;
import com.gig.repository.CommentsRepository;
import com.gig.repository.LikeRepository;
import com.gig.repository.MemberRepository;
import com.gig.repository.PostsRepository;
import com.gig.service.PostService;
import com.gig.service.impl.PostsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public ResponseEntity<PostDto> createPost(CreatePostDto createPostDto, HttpServletRequest request) {
        PostDto responseDto = new PostDto();
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
            List<Posts> posts;
            if(limit>0) {
                posts = postsRepository.findAllPostsLimit(limit, offset);
            }else {
                posts = postsRepository.findAllPosts();
            }
            List<PostDto> postDtoList = emptyIfNull(posts).stream()
                        .map(post -> postsServiceImpl.setPostDetails(loggedInMember, post))
                        .toList();
            paginationDto.setPostDto(postDtoList);
            if(limit>0){
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

    @Override
    public ResponseEntity<PaginationDto> getCommentsByPostId(String postId, int limit, int offset, HttpServletRequest request) {
        PaginationDto paginationDto = new PaginationDto();
        List<CommentsDto> commentsDtoList = new ArrayList<>();
        try{
            applicationUtilities.getLoggedInUser(request);
            List<Comments> comments = null;
            if (limit > 0) {
                comments = commentsRepository.findByPostIdLimit(postId,limit,offset);
            }
            else {
                comments = commentsRepository.findByPostId(postId);
            }
            emptyIfNull(comments).forEach(comment->{
                CommentsDto commentsDto = new CommentsDto();
                BeanUtils.copyProperties(comment,commentsDto);
                Member member = memberRepository.findByIdAndIsDeleted(comment.getMember().getId().toString(), false);
                if (ObjectUtils.isNotEmpty(member)) {
                    commentsDto.setUsername(member.getFirstName()+" "+member.getLastName());
                    commentsDto.setMemberId(member.getId().toString());
                    commentsDto.setProfileUrl(member.getImageUrl());
                }
                commentsDtoList.add(commentsDto);
                paginationDto.setCommentsDto(commentsDtoList);
                if(limit>0) {
                    paginationDto.setTotalCount(commentsDtoList.size());
                    paginationDto.setPerPageCount(commentsRepository.findCommentCount(postId));
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            CommentsDto commentsDto = new CommentsDto();
            commentsDto.setMessage(ex.getMessage());
            commentsDtoList.add(commentsDto);
            paginationDto.setCommentsDto(commentsDtoList);
            return new ResponseEntity<>(paginationDto,HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(paginationDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResponseDTO<LikeResponseDTO>> getLikesByPostId(String postId, int page, int size, HttpServletRequest request) {
        int offset = Math.max(0, (page - 1) * size);
        List<LikeResponseDTO> results = likeRepository.findLikesByPostIdWithDetails(postId, offset, size);
        long totalLikes = likeRepository.countLikesByPostId(postId);
        int totalPages = (int) Math.ceil((double) totalLikes / size);
        PageResponseDTO<LikeResponseDTO> pageResponse = applicationUtilities.createPageResponse(results, totalPages, size, totalLikes);
        return ResponseEntity.ok(pageResponse);
    }

    @Override
    public long getTotalLikes(String postId) {
        return likeRepository.countLikesByPostId(postId);
    }
}
