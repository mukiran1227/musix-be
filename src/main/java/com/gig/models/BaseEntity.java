package com.gig.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "createdAt")
    private LocalDateTime creationTimestamp;
    @Column(name = "updatedAt")
    private LocalDateTime updateTimestamp;
    private Boolean isDeleted = Boolean.FALSE;
    @PreUpdate
    private void updateTimestamp(){
        this.updateTimestamp = LocalDateTime.now();
    }
    @PrePersist
    private void creationTimestamp(){
        this.creationTimestamp = LocalDateTime.now();
        this.updateTimestamp = LocalDateTime.now();
    }
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID createdBy;
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID updatedBy;
}
