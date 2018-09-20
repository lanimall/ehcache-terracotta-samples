package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.dao.QuestionRepository;
import com.github.lanimall.samples.ehcache2.dao.SurveyRepository;
import com.github.lanimall.samples.ehcache2.domain.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@Service
@Transactional (readOnly = true)
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public Survey findSurvey(Long surveyId){
        return surveyRepository.findOne(surveyId);
    }

    @Override
    @Transactional
    public void deleteAllSurveysAndQuestions(){
        surveyRepository.deleteAll();
    }
}
