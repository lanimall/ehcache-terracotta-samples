package com.github.lanimall.samples.ehcache2.cache;

import com.github.lanimall.samples.ehcache2.cache.SurveyResponseCacheWriter;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SurveyResponseWriterFactory extends CacheWriterFactory {
	@Autowired
    JpaRepository<SurveyResponse, Long> repository;

	public CacheWriter createCacheWriter(Ehcache cache, Properties props) {
        return new SurveyResponseCacheWriter(repository);
	}
}
