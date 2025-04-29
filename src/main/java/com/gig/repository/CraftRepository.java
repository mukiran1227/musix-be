package com.gig.repository;

import com.gig.models.Craft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CraftRepository extends JpaRepository<Craft, UUID> {

    @Query(value = "select * from craft c where c.is_deleted=false ",nativeQuery = true)
    List<Craft> fetchAllCrafts();

    @Query(value = "select * from craft c where id=:craftId and is_deleted=false ",nativeQuery = true)
    Craft findByIdAndIsDeleted(@Param("craftId")String craftId);
}
