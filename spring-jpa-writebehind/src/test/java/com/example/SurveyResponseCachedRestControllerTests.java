package com.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lanimall.samples.ehcache2.Application;
import com.github.lanimall.samples.ehcache2.domain.Answer;
import com.github.lanimall.samples.ehcache2.domain.Question;
import com.github.lanimall.samples.ehcache2.domain.Survey;
import com.github.lanimall.samples.ehcache2.domain.SurveyResponse;
import com.github.lanimall.samples.ehcache2.service.SurveyBulkInitService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by fabien.sanglier on 10/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SurveyResponseCachedRestControllerTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SurveyBulkInitService surveyBulkInitService;

    private List<Survey> surveys;

    private List<SurveyResponse> surveyResponses;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        ((MappingJackson2HttpMessageConverter)mappingJackson2HttpMessageConverter).setObjectMapper(mapper);

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.surveyBulkInitService.deleteAllSurveyResponsesAndAnswers();
        this.surveyBulkInitService.deleteAllSurveysAndQuestions();

        //build the sample surveys with questions
        surveys = surveyBulkInitService.addSurveysAndQuestions(2,10);

        //add some sample responses with answers
        surveyResponses = surveyBulkInitService.addSurveyResponses(2);
    }

    @After
    public void destroy() throws Exception {
        this.surveyBulkInitService.deleteAllSurveyResponsesAndAnswers();
        this.surveyBulkInitService.deleteAllSurveysAndQuestions();
        surveys = null;
        surveyResponses = null;
    }

    @Test
    public void readSingleSurveyResponseById() throws Exception {
        String uri = String.format("/surveys/%d/responses/%d", surveys.get(0).getId(), surveyResponses.get(0).getId());

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType)
                );
    }

    @Test
    public void readSingleSurveyResponseByRespondentId() throws Exception {
        String uri = String.format("/surveys/%d/respondents/%d", surveys.get(0).getId(), surveyResponses.get(0).getRespondentID());

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType)
                );
    }

    @Test
    public void surveyNotFound() throws Exception {
        String uri = String.format("/surveys/%d/responses", -1);

        mockMvc.perform(post(uri)
                .content(json(new SurveyResponse(null)))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createSurveyResponse() throws Exception {
        Survey survey = surveys.get(0);
        Long newRespondentId = surveyResponses.get(surveyResponses.size() - 1).getRespondentID()+1;

        String uri = String.format("/surveys/%d/responses", survey.getId());

        SurveyResponse surveyResponse = new SurveyResponse(surveys.get(0).getId(), newRespondentId);

        //answers
        int answerCounter = 0;
        List<Answer> answers = new ArrayList(survey.getQuestions().size());
        for(Question question : survey.getQuestions()){
            Answer answer = new Answer("Some answer " + answerCounter + " to question id {" + question.getId() + "}", null, new Question(question.getId()));
            answers.add(answer);
            answerCounter++;
        }
        surveyResponse.setAnswers(answers);

        String surveyResponseJson = json(surveyResponse);

        this.mockMvc.perform(post(uri)
                .contentType(contentType)
                .content(surveyResponseJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
