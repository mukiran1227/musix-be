package com.gig.repository;

import com.gig.dto.FollowersDto;
import com.gig.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {

    @Query(value = "select * from follow f where f.follower=:followerId and f.followed=:followingId ",nativeQuery = true)
    Follow findByFollowerAndFollowed(@Param("followerId") String followerId, @Param("followingId") String followingId);

    @Query(value = "select m.id as id, CONCAT(m.first_name,' ',m.last_name) as username ,m.image_url as profileUrl from follow f inner join member m on f.follower=m.id and m.is_deleted=false  where f.followed=:memberId and f.is_deleted=false ",nativeQuery = true)
    List<FollowersDto> fetchFollwerList(@Param("memberId") String memberId);

    @Query(value = "select m.id as id, CONCAT(m.first_name,' ',m.last_name) as username ,m.image_url as profileUrl from follow f inner join member m on f.follower=:memberId and m.is_deleted=false  where f.followed=m.id and f.is_deleted=false ",nativeQuery = true)
    List<FollowersDto> fetchFollowingList(@Param("memberId") String memberId);

    @Query(value = "select count(f.id) from follow f where f.followed=:memberId and f.is_deleted=false ",nativeQuery = true)
    int fetchFollowersCount(@Param("memberId") String memberId);

    @Query(value = "select count(f.id) from follow f where f.follower=:memberId and f.is_deleted=false ",nativeQuery = true)
    int fetchFollowingCount(@Param("memberId") String memberId);

    @Query(value = "select * from follow f where f.follower=:followerId and f.followed=:followingId and f.is_deleted=false ",nativeQuery = true)
    Follow checkIsFollowing(@Param("followerId") String followerId, @Param("followingId") String followingId);

}
