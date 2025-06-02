package com.gig.repository;

import com.gig.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    @Query(value = "select m.* from member m where m.email_address=:emailAddress and m.is_deleted=:isFalse ",nativeQuery = true)
    Member findByEmailAddress(@Param("emailAddress") String emailAddress, @Param("isFalse") boolean isFalse);

    @Query(value = "select m.* from member m where m.id=:memberId and m.is_deleted=:isFalse ",nativeQuery = true)
    Member findByIdAndIsDeleted(@Param("memberId") String memberId, @Param("isFalse") boolean isFalse);

     @Query(value = "select m.* from member m where m.email_address=:emailAddress and m.otp=:otp and m.is_deleted=:isFalse ",nativeQuery = true)
     Member findByEmailAddressOtp(@Param("emailAddress") String emailAddress, @Param("otp") Integer otp, @Param("isFalse") boolean isFalse);
}
