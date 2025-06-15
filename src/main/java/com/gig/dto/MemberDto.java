package com.gig.dto;

import com.gig.models.Member;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class MemberDto extends BaseResponseDto{
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String location;
    private Boolean isVerified=Boolean.FALSE;
    private int followersCount;
    private int followingCount;
    private int postCount;
    private String imageUrl;
    private String bio;
    private String memberType;
    private String username;
    private String craft;
    private Boolean isFollowing =Boolean.FALSE;
}
