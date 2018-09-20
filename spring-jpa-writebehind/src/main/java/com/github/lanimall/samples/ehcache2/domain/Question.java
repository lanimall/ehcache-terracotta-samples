package com.github.lanimall.samples.ehcache2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditColumns;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditListener;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditableColumns;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by FabienSanglier on 5/27/15.
 */
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "questions")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question implements AuditableColumns, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name="question_id")
    private Long id;

    @Column(nullable = false, length = 2147483647)
    private String questionText;

    @ManyToOne (optional = false, targetEntity = Survey.class)
    @JoinColumn (name = "survey_id")
    @JsonIgnore
    private Survey survey;

    @Embedded
    @JsonIgnore
    private AuditColumns auditColumns;

    protected Question(){}; //JPA spec

    public Question(Long id){
        this.id = id;
    };

    public Question(String questionText, Survey survey){
        this.questionText = questionText;
        this.survey = survey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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

        Question question = (Question) o;

        if (auditColumns != null ? !auditColumns.equals(question.auditColumns) : question.auditColumns != null)
            return false;
        if (id != null ? !id.equals(question.id) : question.id != null) return false;
        if (questionText != null ? !questionText.equals(question.questionText) : question.questionText != null)
            return false;
        if (survey != null ? !survey.equals(question.survey) : question.survey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (survey != null ? survey.hashCode() : 0);
        result = 31 * result + (auditColumns != null ? auditColumns.hashCode() : 0);
        return result;
    }
}
