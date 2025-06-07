package com.gig.repository;

import com.gig.models.Events;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @Query(value = "SELECT * FROM events WHERE category = :category AND is_deleted = false ORDER BY start_date_time desc limit :limit offset :offset", nativeQuery = true)
    List<Events> findByCategoryAndIsDeletedFalse(@Param("category") String category, @Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM events WHERE category = :category AND is_deleted = false", nativeQuery = true)
    long countByCategory(@Param("category") String category);

    @Query(value = "SELECT * FROM events WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Events findByIdAndIsDeletedFalse(@Param("id") String id);

}
