package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;

/**
 * Created by fabien.sanglier on 10/31/16.
 */

public interface SurveyResponseService {
    SurveyResponse saveFullSurveyResponse(Survey survey, SurveyResponse surveyResponse);

    SurveyResponse findSurveyResponse(Survey survey, Long respondentId);

    SurveyResponse findSurveyResponse(Long surveyResponseId);

    void deleteAllSurveyResponsesAndAnswers();
}
