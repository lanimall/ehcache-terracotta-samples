package com.github.lanimall.domain;

import org.hibernate.CacheMode;

public class QueryCacheProperty {

    private final boolean cachable;
    private CacheMode cacheMode = CacheMode.NORMAL;
    private String cacheRegion;

    public QueryCacheProperty(boolean cachable,
                              CacheMode cacheMode, String cacheRegion) {
        super();
        this.cachable = cachable;
        this.cacheMode = cacheMode;
        this.cacheRegion = cacheRegion;
    }

    public QueryCacheProperty(boolean cachable, String cacheRegion) {
        super();
        this.cachable = cachable;
        this.cacheRegion = cacheRegion;
    }


    public QueryCacheProperty(boolean cachable) {
        super();
        this.cachable = cachable;
    }

    public boolean isCachable() {
        return cachable;
    }

    public CacheMode getCacheMode() {
        if (cacheMode == null)
            return CacheMode.NORMAL;
        return cacheMode;
    }

    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    public String getCacheRegion() {
        return cacheRegion;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }
}
