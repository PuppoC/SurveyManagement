package com.example.surveymanagement;

import Classes.Survey;
import Classes.User;
import Enums.AccessLevel;
import Handlers.StorageHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.io.IOException;
import java.util.*;

public class SurveyListController {

    @FXML VBox surveyContainer;
    @FXML GridPane surveyTemplate;
    @FXML Button createSurveyButton;
//    @FXML ComboBox<SortBy> sortByComboBox;
    @FXML TextField searchTextField;
    private static final String imagePath = "file:src/main/resources/com/example/surveymanagement/Images/";


    private static void showNode(Node node, boolean value){

        node.setManaged(value);
        node.setVisible(value);
        node.setDisable(!value);

    }

    private Button getButton(String text, String buttonImagePath){

        Button button = new Button(text);
        button.setContentDisplay(ContentDisplay.TOP);
        button.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,20));
        button.setPrefWidth(400);// just to make it fill

        ImageView viewButtonImage = new ImageView(imagePath + buttonImagePath);
        viewButtonImage.setFitHeight(30);
        viewButtonImage.setFitWidth(30);

        button.setGraphic(viewButtonImage);

        return button;

    }

    @FXML public void initialize(){

        showNode(surveyTemplate,false);

        User user = App.getSessionUser();

        assert user != null;
        if (user.getAccess() != AccessLevel.ADMIN){
            showNode(createSurveyButton,false);

        }

        List<Survey> allSurveys = StorageHandler.getEachObjectInFolder("Surveys",Survey.class);

        Map<Survey, GridPane> allSurveyTemplates = new HashMap<>();

        for (Survey survey : allSurveys){

            User creatorUser = StorageHandler.readObjectFromFile("Users/"+survey.getCreatorId(),User.class);

            GridPane template = new GridPane();
            template.setPrefSize(920,80);
            template.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));
            template.setPadding(new Insets(10,10,10,10));

            Text nameText = new Text(survey.getName());
            nameText.setWrappingWidth(400);
            nameText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,16));


            Text creatorText = new Text("Created by: " + creatorUser.getUsername());
            creatorText.setWrappingWidth(400);
            creatorText.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR,14));

            template.add(nameText,0,0,1,1);
            template.add(creatorText,0,1,1,1);

            Button completeSurveyButton = getButton("Complete Survey","checklist.png");
            Button resultsButton = getButton("Results","results.png");
            Button editButton = getButton("Edit","edit.png");
            Button deleteButton = getButton("Delete","trash.png");


            completeSurveyButton.setOnAction(actionEvent -> {

                CompleteSurveyController completeSurveyController = new CompleteSurveyController();

                completeSurveyController.setSurvey(survey);

                try {
                    App.setRootManual("completesurvey",completeSurveyController);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            resultsButton.setOnAction(actionEvent -> {

                SurveyResultsController surveyResultsController = new SurveyResultsController();

                surveyResultsController.setSurvey(survey);

                try {
                    App.setRootManual("surveyresults",surveyResultsController);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            editButton.setOnAction(actionEvent -> {

                CreateSurveyController editSurveyController = new CreateSurveyController();

                // to allow create survey controller to prefill survey name, desc, etc
                editSurveyController.setDefaultSurvey(survey);

                try {
                    App.setRootManual("createsurvey",editSurveyController);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                // PREFILL QUESTIONS
                survey.getQuestions().forEach(editSurveyController::addQuestion);

//                for (Question question: survey.getQuestions()){
//
//                    editSurveyController.addQuestion(question);
//
//                }

            });

            deleteButton.setOnAction(actionEvent -> {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setHeaderText("Confirm to delete survey?");
                confirmAlert.setContentText("Deleting: " + survey.getName());

                boolean deleteSuccess;

                Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
                if (confirmResult.isPresent() && (confirmResult.get() == ButtonType.OK)){
                    System.out.println("Deleting survey");

                    deleteSuccess = StorageHandler.deleteFile("Surveys/"+survey.getId());

                }else{
                    System.out.println("Delete survey abandoned");
                    return;

                }

                Alert resultAlert;
                if (deleteSuccess){

                    resultAlert = new Alert(Alert.AlertType.INFORMATION);
                    resultAlert.setHeaderText("Survey deleted successfully!");

                }else{

                    resultAlert = new Alert(Alert.AlertType.WARNING);
                    resultAlert.setHeaderText("Survey deleted unsuccessfully!");

                }
                resultAlert.showAndWait();

                try {
                    App.setRoot("surveylist");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            });

            GridPane buttonsGrid = new GridPane();

            buttonsGrid.addColumn(0,completeSurveyButton);
            buttonsGrid.addColumn(1,resultsButton);
            buttonsGrid.addColumn(2,editButton);
            buttonsGrid.addColumn(3,deleteButton);


            if (user.getAccess() != AccessLevel.ADMIN){

                showNode(resultsButton,false);
                showNode(editButton,false);
                showNode(deleteButton,false);

            }

            template.add(buttonsGrid,1,0,1,2);

            allSurveyTemplates.put(survey,template);

            surveyContainer.getChildren().add(template);


        }

        searchTextField.textProperty().addListener((observableValue, changedFrom, changedTo) -> {

            for (Map.Entry<Survey, GridPane> entry : allSurveyTemplates.entrySet()) {
                Survey survey = entry.getKey();
                GridPane template = entry.getValue();

                showNode(template,survey.getName().toLowerCase().contains(changedTo.toLowerCase()));

            }

        });


    }

    @FXML
    protected void logout() throws IOException {

        App.setSessionUser(null);
        App.setRoot("login");

    }
    @FXML
    protected void goToCreateSurvey() {

        // Need special, to allow controller to be accessed when editing survey
        try {
            App.setRootManual("createsurvey",new CreateSurveyController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
