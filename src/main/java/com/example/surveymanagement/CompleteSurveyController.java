package com.example.surveymanagement;

import Classes.*;
import Enums.QuestionType;
import Handlers.StorageHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompleteSurveyController {

    private Survey survey;
    private List<Answer> allAnswers = new ArrayList<>();
    private User user;

    public void setSurvey(Survey survey){this.survey = survey;}


    @FXML Text surveyTitleText;
    @FXML Text surveyDescriptionText;
    @FXML VBox questionsContainer;


    @FXML VBox paragraphTemplate;
    @FXML VBox MCQTemplate;
    @FXML VBox dropdownTemplate;


    void addQuestion(Question question){

        Answer answer = new Answer();
        answer.setQuestion(question);

        VBox template = new VBox();
        template.setAlignment(Pos.TOP_CENTER);
        template.setFillWidth(true);
        template.setSpacing(5);
        template.setPrefWidth(300);
        template.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));
        template.setPadding(new Insets(5,5,5,5));

        Text questionNameText = new Text();
        questionNameText.setText(question.getName());
        questionNameText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,16));

        template.getChildren().add(questionNameText);

        Node valueToAdd;

        switch (question.getType()){

            case Paragraph,Numbers -> {

                TextArea valueTextArea = new TextArea();
                valueTextArea.setPromptText("Answer Here");
                valueTextArea.setWrapText(true);
                valueTextArea.setPrefHeight(50);

                //Number restriction
                if (question.getType() == QuestionType.Numbers){

                    TextFormatter<String> numberFormatter = new TextFormatter<>(change -> {
                        if (change.isContentChange()) {
                            if (!change.getControlNewText().matches("\\d*")) {
                                return null;
                            }
                        }
                        return change;
                    });

                    valueTextArea.setTextFormatter(numberFormatter);

                }

                valueTextArea.textProperty().addListener((observableValue, changedFrom, changedTo) -> answer.setValue(changedTo));

                valueToAdd = valueTextArea;

            }

            case MCQ -> {

                VBox valueVBox = new VBox();
                valueVBox.setAlignment(Pos.TOP_CENTER);

                ToggleGroup toggleGroup = new ToggleGroup();

                for (String value : question.getValues()){

                    RadioButton radio = new RadioButton();
                    radio.setText(value);
                    radio.setToggleGroup(toggleGroup);

                    valueVBox.getChildren().add(radio);

                }

                toggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> answer.setValue(((ToggleButton) newToggle).getText()));

                valueToAdd = valueVBox;

            }

            case Checkbox -> {

                VBox valueVBox = new VBox();
                valueVBox.setAlignment(Pos.TOP_CENTER);

                List<String> answerList = new ArrayList<>();

                for (String value : question.getValues()){

                    CheckBox checkBox = new CheckBox();
                    checkBox.setText(value);

                    checkBox.selectedProperty().addListener((observable, oldSelected, newSelected) -> {
                        if (newSelected){
                            answerList.add(value);
                        }else{
                            answerList.remove(value);
                        }
                        answer.setValue(answerList);
                    });

                    valueVBox.getChildren().add(checkBox);

                }

                valueToAdd = valueVBox;

            }

            case Dropdown -> {

                ComboBox<String> valueComboBox = new ComboBox<>();
                valueComboBox.setPrefWidth(9999);

                valueComboBox.getItems().addAll(question.getValues());

                valueComboBox.valueProperty().addListener((observable, oldValue, newValue) -> answer.setValue(newValue));

                valueToAdd = valueComboBox;
            }

            default -> {

                System.err.println("No set question type to display");
                return;

            }

        }

        template.getChildren().add(valueToAdd);

        allAnswers.add(answer);

        questionsContainer.getChildren().add(template);

    }


    @FXML void initialize(){
        user = App.getSessionUser();

        questionsContainer.getChildren().removeAll(paragraphTemplate,MCQTemplate,dropdownTemplate); // remove useless templates


        surveyTitleText.setText(survey.getName());
        surveyDescriptionText.setText(survey.getDesc());

        survey.getQuestions().forEach(this::addQuestion);

    }


    @FXML protected void onSubmitButton() throws IOException {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setHeaderText("Confirm to submit response?");


        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if (confirmResult.isPresent() && (confirmResult.get() == ButtonType.CANCEL)){
            System.out.println("Submit cancelled");
            return;
        }

        Submission submission = new Submission();
        submission.setAnswers(allAnswers);
        submission.setUserId(user.getId());

        survey.getSubmissions().add(submission);
        StorageHandler.writeObjectToFile(survey,"Surveys/"+survey.getId().toString());


        Alert submittedAlert = new Alert(Alert.AlertType.INFORMATION);
        submittedAlert.setHeaderText("Survey Submitted Successfully!");
        submittedAlert.showAndWait();

        System.out.println("Survey Submitted");

        App.setRoot("surveylist");


    }

    @FXML protected void goToSurveyList() throws IOException {App.setRoot("surveylist");}

}
