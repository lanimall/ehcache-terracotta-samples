package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;

import java.util.List;

/**
 * Created by fabien.sanglier on 10/31/16.
 */

public interface SurveyService {
    SurveyResponse saveFullSurveyResponse(Survey survey, SurveyResponse surveyResponse);

    Survey findSurvey(Long surveyId);

    SurveyResponse findSurveyResponse(Survey survey, Long respondentId);

    SurveyResponse findSurveyResponse(Long surveyResponseId);

    List<Survey> addSurveysAndQuestions(int surveyCount, int questionCount);

    List<SurveyResponse> addSurveyResponses(int respondentsCount);

    void deleteAllSurveysAndQuestions();

    void deleteAllSurveyResponsesAndAnswers();
}
