package com.github.lanimall.samples.ehcache2.rest;

import com.github.lanimall.samples.ehcache2.dao.SurveyRepository;
import com.github.lanimall.samples.ehcache2.dao.SurveyResponseRepository;
import com.github.lanimall.samples.ehcache2.domain.Answer;
import com.github.lanimall.samples.ehcache2.domain.Question;
import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import com.github.lanimall.samples.ehcache2.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabien.sanglier on 10/31/16.
 */

@RestController
@RequestMapping("/surveys/{surveyId}")
public class SurveyResponseRestController {

    @Autowired
    SurveyService surveyService;

    @RequestMapping(value = "/responses", method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable Long surveyId, @RequestBody SurveyResponse input) {
        Survey survey = validateSurvey(surveyId);
        validateSurveyResponseExists(survey, input.getRespondentID());

        SurveyResponse result = surveyService.saveFullSurveyResponse(survey, input);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{responseId}")
                .buildAndExpand(result.getId()).toUri());

        return new ResponseEntity<>(result, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/respondents/{respondentId}", method = RequestMethod.GET)
    SurveyResponse readSurveyResponseByRespondent(@PathVariable Long surveyId, @PathVariable Long respondentId) {
        Survey survey = validateSurvey(surveyId);
        return surveyService.findSurveyResponse(survey,respondentId);
    }

    @RequestMapping(value = "/responses/{responseId}", method = RequestMethod.GET)
    SurveyResponse readSurveyResponseById(@PathVariable Long surveyId, @PathVariable Long responseId) {
        Survey survey = validateSurvey(surveyId);
        return surveyService.findSurveyResponse(responseId);
    }

    private Survey validateSurvey(Long surveyId) {
        Survey survey = null;
        if(null == (survey = surveyService.findSurvey(surveyId))){
            throw new SurveyNotFoundException((surveyId));
        }
        return survey;
    }

    private SurveyResponse validateSurveyResponseExists(Survey survey, Long respondentId) {
        SurveyResponse surveyResponse = null;
        if(null != (surveyResponse = surveyService.findSurveyResponse(survey,respondentId))){
            throw new SurveyResponseExistException(survey.getId(), respondentId);
        }
        return surveyResponse;
    }
}
