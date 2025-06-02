/*
package com.gig.repository;

import com.gig.models.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, UUID> {

    @Query(value = "select * from collaboration c where c.is_deleted=false and c.id=:collaborationId ",nativeQuery = true)
    Collaboration findByIdAndIsDeleted(@Param("collaborationId") String collaborationId);

    @Query(value = "select * from collaboration c where c.is_deleted=false and c.created_by=:memberId ",nativeQuery = true)
    List<Collaboration> findByMemberId(@Param("memberId") String memberId);
}
*/
