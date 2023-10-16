package com.example.surveymanagement;

import Classes.User;
import javafx.fxml.FXML;
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

        App.setSessionUser(null);
        user = null;
        App.setRoot("login");

    }


}