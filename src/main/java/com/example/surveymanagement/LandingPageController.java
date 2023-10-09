package com.example.surveymanagement;

import Classes.User;
import Enums.AccessLevel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;


public class LandingPageController {

    private static User user;

    @FXML Text welcomeText;


    @FXML
    public void initialize() {

        if (user != null){

            System.out.println("Already signed in, not initialising user object");

        }else{

            user = App.getSessionUser();

            if (user == null){
                System.out.println("User not found, cannot go to landing page");
                return;

            }

        }

        welcomeText.setText("Welcome, " + user.getUsername() + "!");



    }


    @FXML
    protected void goToSurveyList() throws IOException {App.setRoot("surveylist");}


    @FXML
    protected void logout() throws IOException {

        user = null;
        App.setRoot("login");

    }

    @FXML
    protected void goToCreateSurvey() throws IOException {

        // Need special, to allow controller to be accessed when editing survey
        try {
            App.setRootManual("createsurvey",CreateSurveyController.class,new CreateSurveyController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




}