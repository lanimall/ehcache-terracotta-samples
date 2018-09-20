package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.domain.Survey;

/**
 * Created by fabien.sanglier on 10/31/16.
 */

public interface SurveyService {
    Survey findSurvey(Long surveyId);

    void deleteAllSurveysAndQuestions();
}
