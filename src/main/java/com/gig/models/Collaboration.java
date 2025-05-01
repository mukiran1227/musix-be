package com.gig.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "collaboration")
@EqualsAndHashCode(callSuper = true)
public class Collaboration extends BaseEntity{
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String name;
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    private String inviteMessage;
    private String emailAddress;
    @Column(name = "imageUrl", columnDefinition = "TEXT")
    private String imageUrl;
    @Column(name = "coverImageUrl",columnDefinition = "TEXT")
    private String coverImageUrl;
    @Column(name = "clientLookingFor", columnDefinition = "TEXT")
    private String clientLookingFor;
    @Column(name = "artistLookingFor", columnDefinition = "TEXT")
    private String artistLookingFor;
}
