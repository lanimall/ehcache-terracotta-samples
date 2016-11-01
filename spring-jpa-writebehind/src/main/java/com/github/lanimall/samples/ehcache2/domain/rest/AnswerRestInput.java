package com.github.lanimall.samples.ehcache2.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.lanimall.samples.ehcache2.domain.Question;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditColumns;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditListener;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditableColumns;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fabien.sanglier on 10/28/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerRestInput {
    private Question question_id;
    private String answerText;

    public AnswerRestInput(Question question_id, String answerText) {
        this.question_id = question_id;
        this.answerText = answerText;
    }

    public Question getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Question question_id) {
        this.question_id = question_id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
