package com.github.lanimall.samples.ehcache2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditColumns;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditListener;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditableColumns;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fabien.sanglier on 10/28/16.
 */
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "answers")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer implements AuditableColumns, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name="answer_id")
    private Long id;

    @ManyToOne (optional = false)
    @JoinColumn (name = "question_id")
    private Question question;

    @Column(nullable = true, length = 2147483647)
    private String answerText;

    @ManyToOne (optional = false)
    @JoinColumn (name = "surveyresponse_id")
    @JsonIgnore
    private SurveyResponse surveyResponse;

    @Embedded
    @JsonIgnore
    private AuditColumns auditColumns;

    protected Answer(){}; //JPA spec

    protected Answer(Long id){
        this.id = id;
    }

    public Answer(String answerText, SurveyResponse surveyResponse, Question question) {
        this.question = question;
        this.answerText = answerText;
        this.surveyResponse = surveyResponse;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answer) {
        this.answerText = answer;
    }

    public SurveyResponse getSurveyResponse() {
        return surveyResponse;
    }

    public void setSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    @Override
    public AuditColumns getAuditColumns() {
        return auditColumns;
    }

    @Override
    public void setAuditColumns(AuditColumns auditColumns) {
        this.auditColumns = auditColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (answerText != null ? !answerText.equals(answer.answerText) : answer.answerText != null) return false;
        if (auditColumns != null ? !auditColumns.equals(answer.auditColumns) : answer.auditColumns != null)
            return false;
        if (id != null ? !id.equals(answer.id) : answer.id != null) return false;
        if (question != null ? !question.equals(answer.question) : answer.question != null) return false;
        if (surveyResponse != null ? !surveyResponse.equals(answer.surveyResponse) : answer.surveyResponse != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (answerText != null ? answerText.hashCode() : 0);
        result = 31 * result + (surveyResponse != null ? surveyResponse.hashCode() : 0);
        result = 31 * result + (auditColumns != null ? auditColumns.hashCode() : 0);
        return result;
    }
}
