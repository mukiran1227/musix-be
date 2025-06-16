package com.gig.repository;

import com.gig.models.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    
    @Query(value = """
        SELECT e.* FROM events e
        WHERE e.is_deleted = false
        AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))
        AND (:upcomingOnly = false OR e.start_date_time > CURRENT_TIMESTAMP)
        AND (:followedByMemberId IS NULL OR e.member_id IN (
            SELECT DISTINCT f.followed FROM follow f WHERE f.follower = :followedByMemberId
        ))
        ORDER BY e.start_date_time ASC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Events> discoverEvents(
        @Param("offset") int offset,
        @Param("limit") int limit,
        @Param("location") String location,
        @Param("upcomingOnly") boolean upcomingOnly,
        @Param("followedByMemberId") String followedByMemberId
    );
    
    @Query(value = """
        SELECT COUNT(*) FROM events e
        WHERE e.is_deleted = false
        AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))
        AND (:upcomingOnly = false OR e.start_date_time > CURRENT_TIMESTAMP)
        AND (:followedByMemberId IS NULL OR e.member_id IN (
            SELECT DISTINCT f.followed FROM follow f WHERE f.follower = :followedByMemberId
        ))
    """, nativeQuery = true)
    long countDiscoverEvents(
        @Param("location") String location,
        @Param("upcomingOnly") boolean upcomingOnly,
        @Param("followedByMemberId") String followedByMemberId
    );
    
    @Query(value = """
        SELECT DISTINCT e.* FROM events e
        LEFT JOIN events_tickets et ON e.id = et.events_id
        LEFT JOIN tickets t ON et.tickets_id = t.id
        WHERE e.is_deleted = false
        AND (:category IS NULL OR LOWER(e.category) = LOWER(:category))
        AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))
        AND (:startDate IS NULL OR DATE(e.start_date_time) >= :startDate)
        AND (:endDate IS NULL OR DATE(e.end_date_time) <= :endDate)
        AND (:minPrice IS NULL OR t.price >= :minPrice)
        AND (:maxPrice IS NULL OR t.price <= :maxPrice)
        AND (
            :searchTerm IS NULL 
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(e.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        )
        ORDER BY e.start_date_time ASC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Events> searchEvents(
        @Param("category") String category, @Param("location") String location, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
        @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("searchTerm") String searchTerm, @Param("offset") int offset, @Param("limit") int limit
    );
    
    @Query(value = """
        SELECT COUNT(DISTINCT e.id) FROM events e
        LEFT JOIN events_tickets et ON e.id = et.events_id
        LEFT JOIN tickets t ON et.tickets_id = t.id
        WHERE e.is_deleted = false
        AND (:category IS NULL OR LOWER(e.category) = LOWER(:category))
        AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))
        AND (:startDate IS NULL OR DATE(e.start_date_time) >= :startDate)
        AND (:endDate IS NULL OR DATE(e.end_date_time) <= :endDate)
        AND (:minPrice IS NULL OR t.price >= :minPrice)
        AND (:maxPrice IS NULL OR t.price <= :maxPrice)
        AND (
            :searchTerm IS NULL 
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(e.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        )
    """, nativeQuery = true)
    long countSearchEvents(
        @Param("category") String category, @Param("location") String location, @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("searchTerm") String searchTerm
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
