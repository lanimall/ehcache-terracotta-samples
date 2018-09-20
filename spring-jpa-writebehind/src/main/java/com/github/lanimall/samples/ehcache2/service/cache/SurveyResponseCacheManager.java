package com.github.lanimall.samples.ehcache2.service.cache;

import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Jeff
 * Date: 3/19/14
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SurveyResponseCacheManager implements TypedCacheManager<SurveyResponseCacheManager.SurveyResponseKey, SurveyResponse> {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("#{cacheManager.getEhcache('SurveyResponses')}")
    Ehcache cache;

    @Override
    public void put(final SurveyResponseCacheManager.SurveyResponseKey key, final SurveyResponse val) {

        cache.put(new Element(key, val));
    }

    @Override
    public SurveyResponse get(final SurveyResponseCacheManager.SurveyResponseKey key) {
        Element element = cache.get(key);
        return element == null ? null : (SurveyResponse) element.getObjectValue();
    }

    @Override
    public void remove(final SurveyResponseCacheManager.SurveyResponseKey key) {
        cache.remove(key);
    }

    @Override
    public void removeAll() {
        cache.removeAll();
    }

    public static class SurveyResponseKey implements Serializable {
        private final Long surveyResponseID;
        private final Long surveyID;
        private final Long respondentID;

        public SurveyResponseKey(Long surveyResponseID) {
            this(surveyResponseID, null, null);
        }

        public SurveyResponseKey(Long surveyID, Long surveyResponseID) {
            this(null, surveyID, surveyResponseID);
        }

        protected SurveyResponseKey(Long surveyResponseID, Long surveyID, Long respondentID) {
            this.surveyResponseID = surveyResponseID;
            this.surveyID = surveyID;
            this.respondentID = respondentID;
        }

        public Long getSurveyResponseID() {
            return surveyResponseID;
        }

        public Long getSurveyID() {
            return surveyID;
        }

        public Long getRespondentID() {
            return respondentID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SurveyResponseKey that = (SurveyResponseKey) o;

            if (respondentID != null ? !respondentID.equals(that.respondentID) : that.respondentID != null)
                return false;
            if (surveyID != null ? !surveyID.equals(that.surveyID) : that.surveyID != null) return false;
            if (surveyResponseID != null ? !surveyResponseID.equals(that.surveyResponseID) : that.surveyResponseID != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = surveyResponseID != null ? surveyResponseID.hashCode() : 0;
            result = 31 * result + (surveyID != null ? surveyID.hashCode() : 0);
            result = 31 * result + (respondentID != null ? respondentID.hashCode() : 0);
            return result;
        }
    }
}
