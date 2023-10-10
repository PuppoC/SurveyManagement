package com.example.surveymanagement;

import Classes.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

        stage.setResizable(false);

        scene = new Scene(loadFXML("login"), 960,540);
        stage.setScene(scene);
        stage.show();

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