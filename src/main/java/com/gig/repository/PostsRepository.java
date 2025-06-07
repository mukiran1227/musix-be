package com.gig.repository;

import com.gig.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostsRepository extends JpaRepository<Posts, UUID> {

    @Query(value = "select * from posts p where p.id=:id and p.is_deleted=:isFalse",nativeQuery = true)
    Posts findByPostId(@Param("id") String id, @Param("isFalse") boolean isFalse);

    @Query(value = "select * from posts p where p.is_deleted=false ORDER BY p.created_at DESC  limit :limit offset :offset ",nativeQuery = true)
    List<Posts> findAllPostsLimit(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "select * from posts p where p.member_id=:memberId and p.is_deleted=false ORDER BY p.created_at DESC  limit :limit offset :offset",nativeQuery = true)
    List<Posts> findByMemberId(@Param("memberId") String memberId,@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "select count(p.id) from posts p where p.member_id=:memberId and p.is_deleted=false ",nativeQuery = true)
    int findByMemberIdCount(@Param("memberId") String memberId);

    @Query(value = "select count(p.id) from posts p where p.is_deleted=false ",nativeQuery = true)
    int findAllPostsCount();

    @Query(value = "select * from posts p where p.is_deleted=false ORDER BY p.created_at DESC ",nativeQuery = true)
    List<Posts> findAllPosts();

    @Query(value = "select count(p.id) from posts p where p.member_id=:memberId and p.is_deleted=false",nativeQuery = true)
    int countByMemberIdAndIsDeletedFalse(@Param("memberId") String memberId);

}
