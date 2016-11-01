package com.github.lanimall.samples.ehcache2.domain.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResponseRestInput {
    private Long survey_id;
    private Long respondent_id;
    private Collection<AnswerRestInput> answerRestInputs;

    public SurveyResponseRestInput(Long survey_id, Long respondent_id) {
        this.survey_id = survey_id;
        this.respondent_id = respondent_id;
    }

    public Long getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(Long survey_id) {
        this.survey_id = survey_id;
    }

    public Long getRespondent_id() {
        return respondent_id;
    }

    public void setRespondent_id(Long respondent_id) {
        this.respondent_id = respondent_id;
    }

    public Collection<AnswerRestInput> getAnswerRestInputs() {
        return answerRestInputs;
    }

    public void setAnswerRestInputs(Collection<AnswerRestInput> answerRestInputs) {
        this.answerRestInputs = answerRestInputs;
    }
}
