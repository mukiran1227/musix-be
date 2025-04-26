package com.gig.serviceImpl;

import com.gig.dto.BaseResponseDto;
import com.gig.dto.CommentsDto;
import com.gig.dto.CreatePostDto;
import com.gig.dto.PostDto;
import com.gig.dto.TaggedMemberDto;
import com.gig.mappers.PostMapper;
import com.gig.models.Attachments;
import com.gig.models.Comments;
import com.gig.models.Likes;
import com.gig.models.Member;
import com.gig.models.Posts;
import com.gig.repository.CommentsRepository;
import com.gig.repository.LikeRepository;
import com.gig.repository.MemberRepository;
import com.gig.repository.PostsRepository;
import com.gig.service.PostService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gig.applicationUtilities.ApplicationConstants.COMMENT_ADDED;
import static com.gig.applicationUtilities.ApplicationConstants.LIKED;
import static com.gig.applicationUtilities.ApplicationConstants.POST_CREATED;
import static com.gig.applicationUtilities.ApplicationConstants.POST_UPDATED;
import static com.gig.applicationUtilities.ApplicationConstants.UNLIKE;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Service
public class PostsServiceImpl implements PostService {


    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Override
    public BaseResponseDto createPost(CreatePostDto createPostDto, Member member) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Posts posts = new Posts();
            posts.setDescription(createPostDto.getDescription());
            posts.setLocation(createPostDto.getLocation());
            List<Attachments> attachmentsList = emptyIfNull(createPostDto.getAttachments()).stream()
                    .map(dto -> {
                        Attachments attachment = new Attachments();
                        BeanUtils.copyProperties(dto, attachment);
                        return attachment;
                    }).toList();
            posts.setAttachments(attachmentsList);
            Set<Member> taggedMembers = emptyIfNull(createPostDto.getTaggedMembers())
                    .stream()
                    .map(tagId -> memberRepository.findByIdAndIsDeleted(tagId.toString(), false))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            posts.setTaggedMembers(taggedMembers);
            posts.setCreatedBy(member.getId());
            posts.setMember(member);
            postsRepository.save(posts);
            responseDto.setMessage(POST_CREATED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return responseDto;
    }

    @Override
    public PostDto getPost(String postId, Member loggedInMember) {
        PostDto postDto = new PostDto();
        try{
            Posts posts = postsRepository.findByPostId(postId,false);
            postDto = setPostDetails(loggedInMember,posts);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return postDto;
    }

    public PostDto setPostDetails(Member loggedInMember,Posts posts) {
        PostDto postDto = new PostDto();
        try {
            postDto = PostMapper.INSTANCE.entityToDto(posts);
            Set<TaggedMemberDto> taggedMemberDto = emptyIfNull(posts.getTaggedMembers())
                    .stream().map(tag -> memberRepository.findByIdAndIsDeleted(tag.getId().toString(), false))
                    .filter(Objects::nonNull).map(member -> {
                        TaggedMemberDto dto = new TaggedMemberDto();
                        dto.setId(member.getId());
                        dto.setName(member.getFirstName() + " " + member.getLastName());
                        dto.setImageUrl(member.getImageUrl());
                        return dto;
                    }).collect(Collectors.toSet());
            postDto.setTaggedMembers(taggedMemberDto);
            Member member = memberRepository.findByIdAndIsDeleted(posts.getMember().getId().toString(), false);
            if (ObjectUtils.isNotEmpty(member)) {
                postDto.setMemberId(member.getId().toString());
                postDto.setUsername(member.getFirstName() + " " + member.getLastName());
                postDto.setProfileUrl(member.getImageUrl());
            }
            int count = likeRepository.findLikeCount(posts.getId().toString());
            postDto.setLikeCount(count);
            Boolean like = likeRepository.checkUserIsLiked(posts.getId().toString(), loggedInMember.getId().toString());
            if (BooleanUtils.isTrue(like)) {
                postDto.setIsLiked(true);
            }
            int commentCount = commentsRepository.findCommentCount(posts.getId().toString());
            postDto.setCommentCount(commentCount);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return postDto;
    }

    @Override
    public BaseResponseDto updatePost(Posts posts, Member member, CreatePostDto createPostDto) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            if(ObjectUtils.isNotEmpty(createPostDto.getDescription())){
                posts.setDescription(createPostDto.getDescription());
            }
            responseDto.setMessage(POST_UPDATED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return responseDto;
    }

    @Override
    public BaseResponseDto likePost(String postId, Member member) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Posts posts = postsRepository.findByPostId(postId,false);
            if(ObjectUtils.isNotEmpty(posts)){
                Likes likes = likeRepository.findLikeByPostId(posts.getId().toString(),member.getId().toString());
                if(ObjectUtils.isEmpty(likes)){
                    Likes like = new Likes();
                    like.setIsLiked(true);
                    like.setMember(member);
                    likeRepository.save(like);
                    posts.getLikes().add(like);
                    postsRepository.save(posts);
                    responseDto.setMessage(LIKED);
                }else if(BooleanUtils.isTrue(likes.getIsLiked())){
                    likes.setIsLiked(false);
                    likeRepository.save(likes);
                    responseDto.setMessage(UNLIKE);
                }else if(BooleanUtils.isFalse(likes.getIsLiked())){
                    likes.setIsLiked(true);
                    likeRepository.save(likes);
                    responseDto.setMessage(LIKED);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return responseDto;
    }

    @Override
    public BaseResponseDto addComment(String postId, Member member, CommentsDto commentsDto) {
        BaseResponseDto responseDto = new BaseResponseDto();
        try{
            Posts posts = postsRepository.findByPostId(postId,false);
            if(ObjectUtils.isNotEmpty(posts)) {
                Comments comments = new Comments();
                comments.setDescription(commentsDto.getDescription());
                comments.setImageUrl(commentsDto.getImageUrl());
                comments.setMember(member);
                commentsRepository.save(comments);
                posts.getComments().add(comments);
                postsRepository.save(posts);
                responseDto.setMessage(COMMENT_ADDED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return responseDto;
    }

}
