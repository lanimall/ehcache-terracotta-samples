package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import com.github.lanimall.samples.ehcache2.service.cache.SurveyResponseCacheManager;
import com.github.lanimall.samples.ehcache2.service.cache.TypedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@Service ("SurveyResponseServiceWithCache")
@Transactional (readOnly = true)
public class SurveyResponseServiceWithCacheImpl implements SurveyResponseService {
    @Autowired
    TypedCacheManager<SurveyResponseCacheManager.SurveyResponseKey, SurveyResponse> surveyResponseCacheManager;

    @Override
    @Transactional
    public SurveyResponse saveFullSurveyResponse(Survey survey, SurveyResponse surveyResponse){
        surveyResponseCacheManager.put(
                new SurveyResponseCacheManager.SurveyResponseKey(survey.getId(), surveyResponse.getRespondentID()),
                surveyResponse);

        return surveyResponse;
    }

    @Override
    public SurveyResponse findSurveyResponse(Survey survey, Long respondentId){
        return surveyResponseCacheManager.get(
                new SurveyResponseCacheManager.SurveyResponseKey(survey.getId(), respondentId)
        );
    }

    @Override
    public SurveyResponse findSurveyResponse(Long surveyResponseId){
        return surveyResponseCacheManager.get(
                new SurveyResponseCacheManager.SurveyResponseKey(surveyResponseId)
        );
    }

    @Override
    @Transactional
    public void deleteAllSurveyResponsesAndAnswers(){
        surveyResponseCacheManager.removeAll();
    }
}
