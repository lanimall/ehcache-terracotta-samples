package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.dao.SurveyResponseRepository;
import com.github.lanimall.samples.ehcache2.domain.Answer;
import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@Service ("SurveyResponseService")
@Transactional (readOnly = true)
public class SurveyResponseServiceImpl implements SurveyResponseService {
    @Autowired
    SurveyResponseRepository surveyResponseRepository;

    @Override
    @Transactional
    public SurveyResponse saveFullSurveyResponse(Survey survey, SurveyResponse surveyResponse){
        SurveyResponse surveyResponseNew = new SurveyResponse(survey, surveyResponse.getRespondentID());
        surveyResponseRepository.save(surveyResponseNew);

        //Deal with the answers
        List<Answer> answersNew = new ArrayList();
        surveyResponseNew.setAnswers(answersNew);

        //Copy the answer from the input into the new list to save
        //And make sure the answers have the right link to the survey response
        Collection<Answer> answers = surveyResponse.getAnswers();
        for(Answer answer : answers){
            answer.setSurveyResponse(surveyResponseNew);
            answersNew.add(answer);
        }

        surveyResponseRepository.save(surveyResponseNew);

        return surveyResponseNew;
    }

    @Override
    public SurveyResponse findSurveyResponse(Survey survey, Long respondentId){
        return surveyResponseRepository.findBySurveyAndRespondentID(survey, respondentId);
    }

    @Override
    public SurveyResponse findSurveyResponse(Long surveyResponseId){
        return surveyResponseRepository.findOne(surveyResponseId);
    }

    @Override
    @Transactional
    public void deleteAllSurveyResponsesAndAnswers(){
        surveyResponseRepository.deleteAll();
    }
}
