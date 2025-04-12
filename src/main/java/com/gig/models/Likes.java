package com.gig.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "likes")
@EqualsAndHashCode(callSuper = true)
public class Likes extends BaseEntity{
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private Boolean isLiked = Boolean.FALSE;
    @ManyToOne
    private Member member;
}
