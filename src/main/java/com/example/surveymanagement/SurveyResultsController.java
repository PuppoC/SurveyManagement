package com.example.surveymanagement;

import Classes.Answer;
import Classes.Question;
import Classes.Submission;
import Classes.Survey;
import javafx.fxml.FXML;

import java.io.IOException;

public class SurveyResultsController {

    private Survey survey;

    public void setSurvey(Survey survey){this.survey = survey;}

    @FXML void initialize(){
        System.out.println(survey.getName());

        for(Submission submission: survey.getSubmissions()){

            System.out.println("________________________________");

            for (Answer answer: submission.getAnswers()){

                Question question = answer.getQuestion();

                System.out.println("Question:" + question.getName());

                Object actualAnswer = answer.getValue();
                System.out.println("Answer:" +  actualAnswer.toString());

            }




        }


    }

    @FXML
    protected void goToSurveyList() throws IOException {App.setRoot("surveylist");}

}
