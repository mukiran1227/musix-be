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
@Entity(name = "member")
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseEntity{
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "password")
    private String password;
    @Column(name = "city")
    private String city;
    @Column(name = "craft")
    private String craft;
    @Column(name = "intent_of_use")
    private String intentOfUse;
    @Column(name = "is_verified")
    private Boolean isVerified=Boolean.FALSE;
    @Column(name = "image_url",columnDefinition = "TEXT")
    private String imageUrl;
    @Column(name = "otp")
    private Integer otp;
    @Column(name = "bio",columnDefinition = "TEXT")
    private String bio;
    @Column(name = "lookingFor",columnDefinition = "TEXT")
    private String clientLookingFor;
    @Column(name = "artistLookingFor",columnDefinition = "TEXT")
    private String artistLookingFor;
    @Column(name = "cover_image_url",columnDefinition = "TEXT")
    private String coverImageUrl;

}
