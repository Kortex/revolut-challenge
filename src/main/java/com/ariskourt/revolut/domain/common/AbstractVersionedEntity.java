package com.ariskourt.revolut.domain.common;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;

import javax.persistence.*;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public abstract class AbstractVersionedEntity extends PanacheEntityBase {

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Version
    private Integer version;

    @PrePersist
    public void onEntityCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PostUpdate
    public void onEntityUpdate() {
	this.updatedAt = ZonedDateTime.now();
    }

}
