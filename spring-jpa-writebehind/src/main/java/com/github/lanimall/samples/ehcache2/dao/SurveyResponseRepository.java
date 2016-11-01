package com.github.lanimall.samples.ehcache2.dao;

import com.github.lanimall.samples.ehcache2.domain.Answer;
import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.NamedQuery;
import java.util.List;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    SurveyResponse findBySurveyAndRespondentID(Survey survey, Long respondentID);
}
