package com.example.surveymanagement;

import Classes.Survey;
import javafx.fxml.FXML;

import java.io.IOException;

public class CompleteSurveyController {

    private Survey survey;

    public void setSurvey(Survey survey){this.survey = survey;}

    @FXML void initialize(){
        System.out.println(survey.getName());

    }

    @FXML
    protected void goToSurveyList() throws IOException {App.setRoot("surveylist");}

}
