package com.github.lanimall.samples.ehcache2.cache;

import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.CacheDecoratorFactory;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Properties;

/**
 * Created by fabien.sanglier on 11/2/16.
 */
public class SurveyResponseReadThroughFactory extends CacheDecoratorFactory {
    @Autowired
    JpaRepository<SurveyResponse, Long> repository;

    @Override
    public Ehcache createDecoratedEhcache(Ehcache ehcache, Properties properties) {
        return new SelfPopulatingCache(ehcache, new CacheEntryFactory() {
            @Override
            public Object createEntry(Object key) throws Exception {
                return repository.findOne((Long) key);
            }
        });
    }

    @Override
    public Ehcache createDefaultDecoratedEhcache(Ehcache ehcache, Properties properties) {
        return null;
    }
}
