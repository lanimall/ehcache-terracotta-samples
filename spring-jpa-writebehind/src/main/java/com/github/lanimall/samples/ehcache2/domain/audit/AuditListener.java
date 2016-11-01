package com.github.lanimall.samples.ehcache2.domain.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by fabien.sanglier on 10/28/16.
 */
public class AuditListener {
    @PrePersist
    void onCreate(Object entity) {
        if(entity instanceof AuditableColumns) {
            AuditableColumns eact = (AuditableColumns)entity;
            if(eact.getAuditColumns() == null) {
                eact.setAuditColumns(new AuditColumns());
            }
            eact.getAuditColumns().setDateCreated(new Timestamp((new Date()).getTime()));
        }
    }

    @PreUpdate
    void onPersist(Object entity) {
        if(entity instanceof AuditableColumns) {
            AuditableColumns eact = (AuditableColumns)entity;
            if(eact.getAuditColumns() == null) {
                eact.setAuditColumns(new AuditColumns());
            }
            eact.getAuditColumns().setDateUpdated(new Timestamp((new Date()).getTime()));
        }
    }
}
