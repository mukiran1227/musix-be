package com.gig.repository;

import com.gig.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, UUID> {

    @Query(value = "SELECT count(c.id) FROM comments c inner join posts_comments pc on c.id=pc.comments_id where pc.posts_id=:postId and c.is_deleted=false ",nativeQuery = true)
    int findCommentCount(@Param("postId") String postId);
}
