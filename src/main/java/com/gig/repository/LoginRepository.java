package com.gig.repository;

import com.gig.enums.LoginStatusEnum;
import com.gig.models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginRepository extends JpaRepository<Login, UUID> {

    @Query(value = "select * from login where token=:authToken and status=:status",nativeQuery = true)
    Login findByTokenAndStatus(@Param("authToken") String authToken, @Param("status") String status);

}
