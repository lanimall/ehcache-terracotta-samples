package com.terracotta.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;

public class SimpleStaticInitCache {

    private static String CACHE_NAME = "TestCache";

    public static void main(String[] args) throws Exception {
        // pass in the number of object you want to generate, default is 100
        int numberOfObjects = Integer.parseInt(args.length == 0 ? "1000": args[0]);
        int numberOfIteration = Integer.parseInt(args.length == 0 ? "2": args[1]);
        int sleepTime = Integer.parseInt(args.length == 0 ? "5": args[2]);

        String ehcachePath = System.getProperty("ehcache.config.path", "ehcache-static.xml");
        String cacheName = System.getProperty("tc.cache.name", CACHE_NAME);

        //create the CacheManager
        CacheManager cacheManager = createCacheManager(ehcachePath);

        //get a handle on the Cache - the name "myCache" is the name of a cache in the ehcache.xml file
        Cache myCache = getCache(cacheManager, cacheName, false);
        if(null != myCache) {
            int count = 0;
            while (count < numberOfIteration) {
                //iterate through numberOfObjects and use the iterator as the key, value does not matter at this time
                for (int i = 0; i < numberOfObjects; i++) {
                    String key = new Integer(i).toString();
                    if (!checkInCache(key, myCache)) {
                        //when putting in the cache, it is as an Element, the key and the value must be serializable
                        myCache.put(new Element(key, "Value"));
                        System.out.println(key + " NOT in cache!!!");
                    }
                    if (sleepTime > 0)
                        Thread.sleep(sleepTime);
                }
                count++;
            }
        } else {
            throw new IllegalArgumentException("Cache " + CACHE_NAME + " could not be found. Check config.");
        }
    }

    //check to see if the key is in the cache
    private static boolean checkInCache(String key, Cache myCache) throws Exception {
        Element element = myCache.get(key);
        boolean returnValue = false;
        if (element != null) {
            System.out.println(key + " is in the cache!!!");
            returnValue = true;
        }
        return returnValue;
    }

    private static Cache getCache(CacheManager cacheManager, String cacheName, boolean createFromDefault) {
        Cache myCache = null;
        if (null != cacheManager) {
            if (cacheManager.cacheExists(cacheName))
                myCache = cacheManager.getCache(cacheName);
            else if (createFromDefault) {
                CacheConfiguration cacheConfiguration = cacheManager.getConfiguration().getDefaultCacheConfiguration();
                cacheConfiguration.setName(cacheName);
                myCache = new Cache(cacheConfiguration);
                cacheManager.addCache(myCache);
            }
        }
        return myCache;
    }

    private static CacheManager createCacheManager(String configLocationToLoad){
        CacheManager cacheManager = null;
        if (null != configLocationToLoad) {
            InputStream inputStream = null;
            try {
                if (configLocationToLoad.indexOf("file:") > -1) {
                    inputStream = new FileInputStream(configLocationToLoad.substring("file:".length()));
                } else if (configLocationToLoad.indexOf("classpath:") > -1) {
                    inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configLocationToLoad.substring("classpath:".length()));
                } else { //default to classpath if no prefix is specified
                    inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configLocationToLoad);
                }

                if (inputStream == null) {
                    throw new FileNotFoundException("File at '" + configLocationToLoad + "' not found");
                }

                cacheManager = CacheManager.create(inputStream);
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
            cacheManager = CacheManager.getInstance();
        }
        return cacheManager;
    }
}
