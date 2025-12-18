package com.samulparliament_be.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    protected  LocalDateTime deletedAt;

    @PrePersist // DB에 저장되기 직전 실행 어노테이션
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate // DB가 수정되기 직전 실행 어노테이션
    public void onUpdate() {
        if (this.deletedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
