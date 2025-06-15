package com.gig.repository;

import com.gig.models.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Events, UUID> {
    @Query(value = "SELECT * FROM events WHERE is_deleted = false ORDER BY start_date_time desc limit :limit offset :offset", nativeQuery = true)
    List<Events> findAllByIsDeletedFalse(@Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM events WHERE is_deleted = false", nativeQuery = true)
    long countAllByIsDeletedFalse();

    @Query(value = "SELECT * FROM events WHERE member_id = :memberId AND is_deleted = false ORDER BY start_date_time desc limit :limit offset :offset", nativeQuery = true)
    List<Events> findByCreatedByAndIsDeletedFalse(@Param("memberId") String memberId, @Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM events WHERE member_id = :memberId AND is_deleted = false", nativeQuery = true)
    long countByCreatedByAndIsDeletedFalse(@Param("memberId") String memberId);

    @Query(value = """
        SELECT * FROM events 
        WHERE category = :category 
        AND is_deleted = false 
        AND (:eventId IS NULL OR id != :eventId) 
        ORDER BY start_date_time desc 
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Events> findByCategoryAndIsDeletedFalse(
        @Param("category") String category, 
        @Param("offset") int offset, 
        @Param("limit") int limit, 
        @Param("eventId") String eventId
    );

    @Query(value = """
        SELECT COUNT(*) FROM events 
        WHERE category = :category 
        AND is_deleted = false 
        AND (:eventId IS NULL OR id != :eventId)
    """, nativeQuery = true)
    long countByCategory(
        @Param("category") String category,
        @Param("eventId") String eventId
    );

    @Query(value = "SELECT * FROM events WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Events findByIdAndIsDeletedFalse(@Param("id") String id);

    @Query(value = """
        SELECT e.* FROM events e 
        INNER JOIN event_bookmarks eb ON e.id = eb.event_id
        WHERE eb.member_id = :memberId
        AND e.is_deleted = false
        """, nativeQuery = true)
    List<Events> findBookmarkedEventsByMemberId(@Param("memberId") String memberId);

    @Query(value = """
    SELECT EXISTS (
        SELECT 1 FROM event_bookmarks 
        WHERE event_id = :eventId 
        AND member_id = :memberId
    )
    """, nativeQuery = true)
    Boolean isEventBookmarkedByMember(@Param("eventId") String eventId, @Param("memberId") String memberId);
    
    @Query(value = """
        SELECT event_id FROM event_bookmarks 
        WHERE member_id = :memberId
    """, nativeQuery = true)
    Set<UUID> findBookmarkedEventIdsByMemberId(@Param("memberId") String memberId);

}
