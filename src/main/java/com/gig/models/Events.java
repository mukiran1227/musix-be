package com.gig.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "events")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Events extends BaseEntity {
    
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String category;
    private String location;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachments> coverImageUrl = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachments> imageUrl = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Tickets> tickets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Performers> performers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "event_bookmarks",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @JsonIgnore
    private Set<Member> bookmarkedBy = new HashSet<>();
    
    private String instructions;
    private String termsAndConditions;
}
