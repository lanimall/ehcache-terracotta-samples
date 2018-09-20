package com.github.lanimall.samples.ehcache2;

import com.github.lanimall.samples.ehcache2.dao.QuestionRepository;
import com.github.lanimall.samples.ehcache2.dao.SurveyRepository;
import com.github.lanimall.samples.ehcache2.dao.SurveyResponseRepository;
import com.github.lanimall.samples.ehcache2.service.SurveyBulkInitService;
import com.github.lanimall.samples.ehcache2.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Autowired
    private SurveyBulkInitService initService;

    @Bean
    CommandLineRunner init(SurveyRepository surveyRepository, QuestionRepository questionRepository, SurveyResponseRepository surveyResponseRepository) {
        return args -> {
            initService.deleteAllSurveyResponsesAndAnswers();

            initService.deleteAllSurveysAndQuestions();

            //build the sample surveys with questions
            initService.addSurveysAndQuestions(10,10);

            //add some sample responses with answers
            //initService.addSurveyResponses(10);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
