package com.github.lanimall.samples.ehcache2.rest;

import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import com.github.lanimall.samples.ehcache2.service.SurveyResponseService;
import com.github.lanimall.samples.ehcache2.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Created by fabien.sanglier on 10/31/16.
 */

@RestController
@RequestMapping("/surveys/{surveyId}/responses/cached/")
public class SurveyResponseCachedRestController {

    @Autowired
    SurveyService surveyService;

    @Autowired
    @Qualifier ("SurveyResponseServiceWithCache")
    SurveyResponseService surveyResponseService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable Long surveyId, @RequestBody SurveyResponse input) {
        Survey survey = validateSurvey(surveyId);
        validateSurveyResponseExists(survey, input.getRespondentID());

        SurveyResponse result = surveyResponseService.saveFullSurveyResponse(survey, input);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{responseId}")
                .buildAndExpand(result.getId()).toUri());

        return new ResponseEntity<>(result, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/respondents/{respondentId}", method = RequestMethod.GET)
    SurveyResponse readSurveyResponseByRespondent(@PathVariable Long surveyId, @PathVariable Long respondentId) {
        Survey survey = validateSurvey(surveyId);
        return surveyResponseService.findSurveyResponse(survey,respondentId);
    }

    @RequestMapping(value = "/{responseId}", method = RequestMethod.GET)
    SurveyResponse readSurveyResponseById(@PathVariable Long surveyId, @PathVariable Long responseId) {
        Survey survey = validateSurvey(surveyId);
        return surveyResponseService.findSurveyResponse(responseId);
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
        if(null != (surveyResponse = surveyResponseService.findSurveyResponse(survey,respondentId))){
            throw new SurveyResponseExistException(survey.getId(), respondentId);
        }
        return surveyResponse;
    }
}