package com.gig.repository;

import com.gig.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, UUID> {

    @Query(value = "SELECT count(c.id) FROM comments c inner join posts_comments pc on c.id=pc.comments_id where pc.posts_id=:postId and c.is_deleted=false ",nativeQuery = true)
    int findCommentCount(@Param("postId") String postId);

    @Query(value = "SELECT c.* FROM comments c INNER JOIN posts_comments pc ON c.id = pc.comments_id WHERE pc.posts_id = :postId AND c.is_deleted = false ORDER BY c.created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Comments> findByPostId(@Param("postId") String postId, @Param("limit") int limit, @Param("offset") int offset);

}
