package com.gig.repository;

import com.gig.models.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Likes, UUID> {

    @Query(value = "SELECT l.* FROM posts p INNER JOIN posts_likes pl INNER JOIN likes l ON l.id = pl.likes_id AND p.id = pl.posts_id WHERE p.id = :postId AND l.member_id =:memberId ",nativeQuery = true)
    Likes findLikeByPostId(@Param("postId") String postId, @Param("memberId") String memberId);

    @Query(value = "select count(l.id) from likes l inner join posts_likes pl on l.id=pl.likes_id where pl.posts_id=:postId AND l.is_liked =true ",nativeQuery = true)
    int findLikeCount(@Param("postId") String postId);

    @Query(value = " SELECT l.is_liked FROM posts p INNER JOIN posts_likes pl INNER JOIN likes l ON l.id = pl.likes_id AND p.id = pl.posts_id WHERE p.id = :postId AND l.member_id = :memberId AND l.is_liked = true ", nativeQuery = true)
    Boolean checkUserIsLiked(@Param("postId") String postId, @Param("memberId") String memberId);
}
