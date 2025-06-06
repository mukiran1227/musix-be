package com.gig.repository;

import com.gig.models.Events;
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
    

    
    @Query(value = "SELECT * FROM events WHERE location = :location AND is_deleted = false", nativeQuery = true)
    List<Events> findByLocationAndIsDeletedFalse(@Param("location") String location);
    

    @Query(value = "SELECT * FROM events WHERE is_deleted = false", nativeQuery = true)
    List<Events> findAllByIsDeletedFalse();
    

    
    @Query(value = "SELECT * FROM events WHERE start_date_time BETWEEN :start AND :end AND is_deleted = false", nativeQuery = true)
    List<Events> findByStartDateTimeBetweenNative(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    

    
    @Query(value = "SELECT * FROM events WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Events findByIdAndIsDeletedFalse(@Param("id") String id);

    @Query(value = "SELECT * FROM events WHERE is_deleted = false " +
            "AND ((:categories IS NULL OR :categories = '[]') OR category IN :categories) " +
            "AND ((:location IS NULL OR :location = '') OR location = :location) " +
            "AND ((:startDate IS NULL OR :startDate = 'Z') OR start_date_time >= :startDate) " +
            "AND ((:endDate IS NULL OR :endDate = '') OR start_date_time <= :endDate) " +
            "AND ((:minPrice IS NULL OR :minPrice = 0) OR EXISTS (SELECT 1 FROM tickets WHERE event_id = events.id AND price >= :minPrice)) " +
            "AND ((:maxPrice IS NULL OR :maxPrice = 0) OR EXISTS (SELECT 1 FROM tickets WHERE event_id = events.id AND price <= :maxPrice)) " +
            "AND ((:searchQuery IS NULL OR :searchQuery = '') OR " +
            "LOWER(name) LIKE CONCAT('%', LOWER(:searchQuery), '%') OR " +
            "LOWER(location) LIKE CONCAT('%', LOWER(:searchQuery), '%') OR " +
            "LOWER(description) LIKE CONCAT('%', LOWER(:searchQuery), '%')) " +
            "ORDER BY start_date_time",
            nativeQuery = true)
    List<Events> searchEvents(
            @Param("categories") List<String> categories,
            @Param("location") String location,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("searchQuery") String searchQuery
    );

}
