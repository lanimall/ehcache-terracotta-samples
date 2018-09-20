package com.github.lanimall.samples.ehcache2.service.cache;

import com.github.lanimall.samples.ehcache2.cache.SurveyResponseReadThroughFactory;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by fabien.sanglier on 11/1/16.
 */
@Service
public class CacheInitConfigurer implements InitializingBean {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    JpaRepository<SurveyResponse, Long> repository;

    @Override
    public void afterPropertiesSet() throws Exception {
//        final Ehcache myCache = cacheManager.getEhcache("SurveyResponses");
//        cacheManager.replaceCacheWithDecoratedCache(
//                myCache,
//                new SelfPopulatingCache(myCache, new SurveyResponseReadThroughFactory(repository))
//        );
    }
}
