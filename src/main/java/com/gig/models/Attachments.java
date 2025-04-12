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
@Entity(name = "attachments")
@EqualsAndHashCode(callSuper = true)
public class Attachments extends BaseEntity{
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @Column(name = "uploaded")
    private Boolean uploaded = Boolean.FALSE;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "upload_Url", columnDefinition = "TEXT")
    private String uploadUrl;
}
