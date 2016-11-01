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
}
