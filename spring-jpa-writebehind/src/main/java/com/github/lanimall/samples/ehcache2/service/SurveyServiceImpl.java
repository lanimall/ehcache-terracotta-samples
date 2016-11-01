package com.github.lanimall.samples.ehcache2.service;

import com.github.lanimall.samples.ehcache2.dao.QuestionRepository;
import com.github.lanimall.samples.ehcache2.dao.SurveyRepository;
import com.github.lanimall.samples.ehcache2.dao.SurveyResponseRepository;
import com.github.lanimall.samples.ehcache2.domain.Answer;
import com.github.lanimall.samples.ehcache2.domain.Question;
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
@Service
@Transactional (readOnly = true)
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    QuestionRepository questionRepository;

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
    public Survey findSurvey(Long surveyId){
        return surveyRepository.findOne(surveyId);
    }

    @Override
    public SurveyResponse findSurveyResponse(Survey survey, Long respondentId){
        return surveyResponseRepository.findBySurveyAndRespondentID(survey,respondentId);
    }

    @Override
    public SurveyResponse findSurveyResponse(Long surveyResponseId){
        return surveyResponseRepository.findOne(surveyResponseId);
    }

    @Override
    @Transactional
    public List<Survey> addSurveysAndQuestions(int surveyCount, int questionCount) {
        List<Survey> surveys = new ArrayList<Survey>();
        for(int i=0; i<surveyCount; i++){
            Survey survey = new Survey("survey_" + i);
            surveyRepository.save(survey);

            List<Question> questions = new ArrayList(10);
            for(int j=0; j<questionCount; j++) {
                questions.add(new Question("question_" + j, survey));
            }
            survey.setQuestions(questions);
            surveyRepository.save(survey);
            surveys.add(survey);
        }
        return surveys;
    }

    @Override
    @Transactional
    public void deleteAllSurveysAndQuestions(){
        surveyRepository.deleteAll();
    }

    @Override
    @Transactional
    public List<SurveyResponse> addSurveyResponses(int respondentsCount) {
        List<SurveyResponse> responses = new ArrayList<SurveyResponse>();
        for(long respondent = 0; respondent < respondentsCount; respondent++){ //each respondent
            Iterable<Survey> surveys = surveyRepository.findAll();
            for(Survey survey : surveys)
            {
                SurveyResponse surveyResponse = new SurveyResponse(survey, respondent);
                surveyResponseRepository.save(surveyResponse);

                int answerCounter = 0;
                List<Answer> answers = new ArrayList(survey.getQuestions().size());
                for(Question question : survey.getQuestions()){
                    answers.add(new Answer("Some answer " + answerCounter + " to question id {" + question.getId() + "}", surveyResponse, question));
                    answerCounter++;
                }
                surveyResponse.setAnswers(answers);
                surveyResponseRepository.save(surveyResponse);

                responses.add(surveyResponse);
            }
        }
        return responses;
    }

    @Override
    @Transactional
    public void deleteAllSurveyResponsesAndAnswers(){
        surveyResponseRepository.deleteAll();
    }
}
