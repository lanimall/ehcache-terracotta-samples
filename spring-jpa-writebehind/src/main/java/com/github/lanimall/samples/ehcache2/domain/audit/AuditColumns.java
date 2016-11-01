package com.github.lanimall.samples.ehcache2.domain.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.sql.Timestamp;

/**
 * Created by fabien.sanglier on 10/28/16.
 */
@Embeddable
public class AuditColumns {
    @Column(name="UPDATE_TS", insertable=false, updatable=true)
    @JsonIgnore
    private Timestamp dateUpdated;

    @Column(name="CREATION_TS", insertable=true, updatable=false)
    @JsonIgnore
    private Timestamp dateCreated;

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}