package com.gig.models;

import com.gig.enums.LoginStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
public class Login extends BaseEntity{
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String token;

    private String browserDetails;

    private LocalDateTime loggedInTime;

    private LocalDateTime loggedOutTime;

    @Enumerated(EnumType.STRING)
    private LoginStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
}
