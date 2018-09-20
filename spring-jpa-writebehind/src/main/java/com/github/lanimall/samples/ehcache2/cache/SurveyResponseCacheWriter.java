package com.github.lanimall.samples.ehcache2.cache;

import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public class SurveyResponseCacheWriter implements CacheWriter {
	private static Logger log = LoggerFactory.getLogger(SurveyResponseCacheWriter.class);

	private final JpaRepository<SurveyResponse, Long> dao;

	public SurveyResponseCacheWriter(JpaRepository dao) {
		super();
		this.dao = dao;
	}

	@Override
	public void throwAway(Element arg0, SingleOperationType arg1,
			RuntimeException arg2) {
		;
	}

	@Override
	public CacheWriter clone(Ehcache arg0) throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public void delete(CacheEntry element) throws CacheException {
		deleteElement(element.getElement());
	}

	private void deleteElement(Element element) throws CacheException {
		if(null != element && element.getObjectKey() instanceof Long)
			dao.delete((Long)element.getObjectKey());
	}

	@Override
	public void deleteAll(Collection<CacheEntry> elements) throws CacheException {
		if(null != elements && elements.size() > 0){
			for(CacheEntry entry : elements){
                deleteElement(entry.getElement());
            }
		}
	}

	@Override
	public void dispose() throws CacheException {
		;
	}

	@Override
	public void init() {
		;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(Element element) throws CacheException {
		if(null == element.getObjectValue())
			deleteElement(element);
		else if(element.getObjectValue() instanceof SurveyResponse)
			dao.save((SurveyResponse)element.getObjectValue());
	}

	@Override
	public void writeAll(Collection<Element> elements) throws CacheException {
		//TODO: should be done in bulk...not the intent of this test app though
		for (Element element : elements) {
			write(element);
		}
	}
}
