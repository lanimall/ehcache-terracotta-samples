package com.github.lanimall.samples.ehcache2.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(Long surveyId) {
        super("Survey Not Found! Could not find survey '" + surveyId + "'.");
    }
}
