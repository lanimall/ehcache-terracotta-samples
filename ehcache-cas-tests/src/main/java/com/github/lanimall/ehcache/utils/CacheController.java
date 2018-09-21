package com.github.lanimall.ehcache.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fabien.sanglier on 9/20/18.
 */
public class CacheController {
    public static final CacheTestType DEFAULT_CACHETEST_TYPE = CacheTestType.LOCAL_HEAP;

    private CacheManager cm;
    private Cache cache;

    public CacheManager getCm() {
        return cm;
    }

    public Cache getCache() {
        return cache;
    }

    public enum CacheTestType {
        LOCAL_HEAP("localheap") {
            public String getCacheConfigPath(){
                return "classpath:ehcache_localheap.xml";
            }

            public String getCacheManagerName(){
                return "EhcacheCounterLocalHeapTests";
            }

            public String getCacheName(){
                return "CounterCache";
            }
        },
        LOCAL_OFFHEAP("localoffheap") {
            public String getCacheConfigPath(){
                return "classpath:ehcache_localoffheap.xml";
            }

            public String getCacheManagerName(){
                return "EhcacheCounterOffheapTest";
            }

            public String getCacheName(){
                return "CounterCache";
            }
        },
        CLUSTERED("clustered") {
            public String getCacheConfigPath(){
                return "classpath:ehcache_distributed.xml";
            }

            public String getCacheManagerName(){
                return "EhcacheCounterDistributedTest";
            }

            public String getCacheName(){
                return "CounterCache";
            }
        };

        private final String propValue;

        CacheTestType(String propValue) {
            this.propValue = propValue;
        }

        public abstract String getCacheConfigPath();
        public abstract String getCacheManagerName();
        public abstract String getCacheName();

        public String getPropValue() {
            return propValue;
        }

        public static CacheTestType valueOfIgnoreCase(String cacheTestTypeStr){
            if(null != cacheTestTypeStr && !"".equals(cacheTestTypeStr)) {
                if (LOCAL_HEAP.propValue.equalsIgnoreCase(cacheTestTypeStr))
                    return LOCAL_HEAP;
                else if (LOCAL_OFFHEAP.propValue.equalsIgnoreCase(cacheTestTypeStr))
                    return LOCAL_OFFHEAP;
                else if (CLUSTERED.propValue.equalsIgnoreCase(cacheTestTypeStr))
                    return CLUSTERED;
                else
                    throw new IllegalArgumentException("CacheTestType [" + ((null != cacheTestTypeStr) ? cacheTestTypeStr : "null") + "] is not valid");
            } else {
                return DEFAULT_CACHETEST_TYPE;
            }
        }
    }

    public void setUpCache(CacheTestType cacheTestType, boolean emptyCache) {
        if(cacheTestType == null)
            cacheTestType = DEFAULT_CACHETEST_TYPE;

        cm = getCacheManager(cacheTestType.getCacheManagerName(), cacheTestType.getCacheConfigPath());
        cache = cm.getCache(cacheTestType.getCacheName());
        if (cache == null) {
            throw new IllegalArgumentException("Could not find the cache " + cacheTestType.getCacheName() + " in " + cacheTestType.getCacheConfigPath());
        }

        if(emptyCache)
            cache.removeAll();
    }

    public void tearDownCache() {
        if(null != cm)
            cm.shutdown();

        cm = null;
        cache = null;
    }

    private CacheManager getCacheManager(String cacheManagerName, String resourcePath) {
        CacheManager cm = null;
        if (null == (cm = CacheManager.getCacheManager(cacheManagerName))) {
            String configLocationToLoad = null;
            if (null != resourcePath && !"".equals(resourcePath)) {
                configLocationToLoad = resourcePath;
            }

            if (null != configLocationToLoad) {
                InputStream inputStream = null;
                try {
                    if (configLocationToLoad.indexOf("file:") > -1) {
                        inputStream = new FileInputStream(configLocationToLoad.substring("file:".length()));
                    } else if (configLocationToLoad.indexOf("classpath:") > -1) {
                        inputStream = this.getClass().getClassLoader().getResourceAsStream(configLocationToLoad.substring("classpath:".length()));
                    } else { //default to classpath if no prefix is specified
                        inputStream = this.getClass().getClassLoader().getResourceAsStream(configLocationToLoad);
                    }

                    if (inputStream == null) {
                        throw new FileNotFoundException("File at '" + configLocationToLoad + "' not found");
                    }

                    cm = CacheManager.create(inputStream);
                } catch (IOException ioe) {
                    throw new CacheException(ioe);
                } finally {
                    if (null != inputStream) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            throw new CacheException(e);
                        }
                        inputStream = null;
                    }
                }
            } else {
                cm = CacheManager.getInstance();
            }
        }

        return cm;
    }
}
