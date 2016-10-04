package com.terracotta.sample;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.*;

/**
 * Created by fabien.sanglier on 10/3/16.
 */
public class CreateCacheDynamically {
    private static String CACHE_MANAGER_NAME = "MyFirstTerracottaCacheMgr";
    private static String CACHE_NAME = "TestCache";
    private static String TC_SERVER_URL = "localhost:9510";
    private static int MAX_ELEMENTS_IN_MEMORY = 100;
    private static int MAX_ENTRIES_IN_CACHE = 10000;

    public static void main(String[] args) throws Exception {
        // pass in the number of object you want to generate, default is 100
        int numberOfObjects = Integer.parseInt(args.length == 0 ? "1000": args[0]);
        int numberOfIteration = Integer.parseInt(args.length == 0 ? "2": args[1]);
        int sleepTime = Integer.parseInt(args.length == 0 ? "5": args[2]);

        //get the url from sys properties
        String tc_url = System.getProperty("tc.connect.url", TC_SERVER_URL);
        String cacheName = System.getProperty("tc.cache.name", CACHE_NAME);

        CacheManager cacheManager = getOrCreateCacheManager(CACHE_MANAGER_NAME, tc_url);

        //get a handle on the Cache - the name "myCache" is the name of a cache in the ehcache.xml file
        Cache myCache = getOrCreateCache(cacheManager, cacheName);
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

    private static CacheManager getOrCreateCacheManager(String cacheManagerName, String tc_url) {
        CacheManager cacheManager = CacheManager.getCacheManager(cacheManagerName);
        if(null == cacheManager){
            TerracottaClientConfiguration terracottaClientConfiguration = new TerracottaClientConfiguration();
            terracottaClientConfiguration.url(tc_url);

            Configuration configuration = new Configuration();
            configuration.setName(cacheManagerName);
            configuration.terracotta(terracottaClientConfiguration);
            configuration.defaultCache(new CacheConfiguration("defaultCache", 100));

            //important: use the CacheManager.create here to register the cachemanager in the internal singleton
            cacheManager = CacheManager.create(configuration);
        }
        return cacheManager;
    }

    private static Cache getOrCreateCache(CacheManager cacheManager, String cacheName) {
        Cache myCache = null;
        if(null != cacheManager) {
            myCache = cacheManager.getCache(cacheName);

            if(null == myCache) {
                CacheConfiguration cacheConfiguration = new CacheConfiguration();
                cacheConfiguration.setName(cacheName);
                cacheConfiguration.setMaxEntriesLocalHeap(MAX_ELEMENTS_IN_MEMORY);
                cacheConfiguration.setTimeToIdleSeconds(50L);
                cacheConfiguration.setTimeToLiveSeconds(300L);
                cacheConfiguration.setMaxEntriesInCache(MAX_ENTRIES_IN_CACHE);

                //TC configuration for the cache
                TerracottaConfiguration terracottaConfiguration = new TerracottaConfiguration();
                NonstopConfiguration nonstopConfiguration = new NonstopConfiguration();
                terracottaConfiguration.addNonstop(nonstopConfiguration);
                TimeoutBehaviorConfiguration timeoutBehaviorConfiguration = new TimeoutBehaviorConfiguration();
                nonstopConfiguration.addTimeoutBehavior(timeoutBehaviorConfiguration);

                terracottaConfiguration.consistency(TerracottaConfiguration.Consistency.EVENTUAL);
                nonstopConfiguration.immediateTimeout(true);
                nonstopConfiguration.setTimeoutMillis(10000);
                timeoutBehaviorConfiguration.setType(TimeoutBehaviorConfiguration.EXCEPTION_TYPE_NAME);

                //adding the terracotta config to the cache
                cacheConfiguration.addTerracotta(terracottaConfiguration);

                cacheConfiguration.validateConfiguration();

                myCache = new Cache(cacheConfiguration);
                cacheManager.addCache(myCache);
            }
        }
        return myCache;
    }
}

