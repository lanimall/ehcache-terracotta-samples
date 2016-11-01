package com.github.lanimall.samples.ehcache2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditColumns;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditListener;
import com.github.lanimall.samples.ehcache2.domain.audit.AuditableColumns;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fabien.sanglier on 10/28/16.
 */
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "surveys")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Survey implements AuditableColumns, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name="survey_id")
    private Long id;

    @Column(nullable = false, length = 4096)
    private String name;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="survey", targetEntity = Question.class)
    private Collection<Question> questions;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="survey", targetEntity = SurveyResponse.class)
    private Collection<SurveyResponse> surveyResponses;

    @Embedded
    @JsonIgnore
    private AuditColumns auditColumns;

    protected Survey(){}; //JPA spec

    public Survey(Long id){
        this.id = id;
    }

    public Survey(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
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
