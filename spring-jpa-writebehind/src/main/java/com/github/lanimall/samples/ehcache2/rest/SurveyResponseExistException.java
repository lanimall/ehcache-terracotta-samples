package com.github.lanimall.samples.ehcache2.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class SurveyResponseExistException extends RuntimeException {
    public SurveyResponseExistException(Long surveyId, Long respondentId) {
        super("Survey Response already found for survey '" + surveyId + "' and respondent '" + respondentId + "'.");
    }
}
