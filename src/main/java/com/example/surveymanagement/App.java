package com.example.surveymanagement;

import Classes.Answer;
import Classes.Submission;
import Classes.Survey;
import Classes.User;
import Handlers.StorageHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class App extends Application {

    private static User sessionUser;
    public static void setSessionUser(User user){sessionUser = user;}
    public static User getSessionUser(){
        if (sessionUser == null){
            System.out.println("No session user to get!!");
            return null;
        }else {
            return sessionUser;
        }
    }

    private static Scene scene;


    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("Starting Application");

//        stage.setResizable(false);

        scene = new Scene(loadFXML("login"), 960,540);
        stage.setScene(scene);
        stage.show();

        System.out.println("Application Running");

// Manually fill in submission
//        Survey survey = StorageHandler.readObjectFromFile("Surveys/" + "0a1cf176-b02f-4684-a79c-8b54d0e51b9c",Survey.class);
//
//        Answer answer0 = new Answer();
//        answer0.setQuestion(survey.getQuestions().get(0));
//        answer0.setValue("Rarely");
//
//        Answer answer1 = new Answer();
//        answer1.setQuestion(survey.getQuestions().get(1));
//        answer1.setValue("Vegan");
//
//        Answer answer2 = new Answer();
//        answer2.setQuestion(survey.getQuestions().get(2));
//        answer2.setValue("Waoooooo anotherrr oneeee");
//
//        List<Answer> allAnswers = new ArrayList<>();
//        allAnswers.add(answer0);
//        allAnswers.add(answer1);
//        allAnswers.add(answer2);
//
//        Submission newSubmission = new Submission();
//        newSubmission.setUserId(UUID.fromString("f7dd3822-b233-41e1-a3a7-970799fcb77d"));
//        newSubmission.setAnswers(allAnswers);
//
//
//
//        survey.getSubmissions().add(newSubmission);
//
//
////
//        StorageHandler.writeObjectToFile(survey,"Surveys/" + survey.getId());

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static <T> void setRootManual(String fxml, T controllerInstance) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        if (controllerInstance != null) {
            fxmlLoader.setController(controllerInstance);
        }else{
            System.err.println("No controller given when loading " + fxml + " manually!!");
        }

        Parent root = fxmlLoader.load();

        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }


}