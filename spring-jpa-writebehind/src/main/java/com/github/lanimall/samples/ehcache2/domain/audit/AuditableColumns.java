package com.github.lanimall.samples.ehcache2.domain.audit;

/**
 * Created by fabien.sanglier on 10/28/16.
 */
public interface AuditableColumns {
    AuditColumns getAuditColumns();
    void setAuditColumns(AuditColumns columns);
}
