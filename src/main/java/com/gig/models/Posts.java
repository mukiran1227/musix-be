package com.gig.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "posts")
@EqualsAndHashCode(callSuper = true)
public class Posts extends BaseEntity{
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachments> attachments = new ArrayList<>();
    @ManyToOne
    private Member member;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comments> comments = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();
}
