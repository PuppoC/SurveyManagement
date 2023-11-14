package com.example.surveymanagement;

import Classes.*;
import Handlers.StorageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SurveyResultsController {

    private Survey survey;
    private int submissionIndex = 0;


    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @FXML
    Text surveyTitleText;
    @FXML
    Text surveyDescriptionText;
    @FXML
    Text userInfoText;
    @FXML VBox indivAnswersContainer;
    @FXML VBox sumAnswersContainer;

    private void resizeTextArea(TextArea textArea) {
        // Create a temporary Text node to measure the text bounds
        Text textNode = new Text(textArea.getText());
        textNode.setFont(Font.font(textArea.getFont().getFamily(), textArea.getFont().getSize()));

        // Calculate the width and height required for the text
        double width = Math.max(textArea.getMinWidth(), textNode.getLayoutBounds().getWidth() + 20);
        double height = Math.max(textArea.getMinHeight(), textNode.getLayoutBounds().getHeight() + 20);

        // Set the new size for the TextArea
        textArea.setPrefSize(width, height);
    }

    private void fillInAnswer(int index) {

        if ((index < 0) || (index > (survey.getSubmissions().size()-1))){
            System.out.println("Index out of bounds");
            return;
        }

        Submission submission = survey.getSubmissions().get(index);

        indivAnswersContainer.getChildren().clear();

        User user = StorageHandler.readObjectFromFile("Users/" + submission.getUserId(), User.class);

        if (user != null) {
            userInfoText.setText(String.format("[%s/%s] Submitted by: %s", index+1, survey.getSubmissions().size(), user.getUsername()));
        }

        for (Answer answer : submission.getAnswers()) {

            Question question = answer.getQuestion();

            VBox template = new VBox();
            template.setAlignment(Pos.TOP_CENTER);
            template.setFillWidth(true);
            template.setSpacing(5);
            template.setPrefWidth(300);
            template.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            template.setPadding(new Insets(5, 5, 5, 5));

            Text questionNameText = new Text();
            questionNameText.setText(question.getName());
            questionNameText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR, 16));

            TextArea valueTextArea = new TextArea();
            valueTextArea.setWrapText(true);
            valueTextArea.setPrefHeight(50);
            valueTextArea.setEditable(false);
            valueTextArea.setText(answer.getValue().toString());
            resizeTextArea(valueTextArea);

            template.getChildren().addAll(questionNameText, valueTextArea);

            indivAnswersContainer.getChildren().add(template);

        }

        submissionIndex = index;


    }



    @FXML
    void initialize() {

        surveyTitleText.setText(survey.getName());
        surveyDescriptionText.setText(survey.getDesc());

        //INIT INDIVIDUAL SECTION
        if (!survey.getSubmissions().isEmpty()) {
            fillInAnswer(submissionIndex);
        }else{
            userInfoText.setText("No Submissions!");
        }



        //INIT SUMMARY SECTION

        //Group submissions by answers
        Map<String, List<Answer>> allQuestionAnswers = new HashMap<>();


        for (Submission submission : survey.getSubmissions()){

            for (Answer answer: submission.getAnswers()){

                if (answer.getValue().toString().isEmpty()){ // ignore empty answers
                    continue;
                }

                Question question = answer.getQuestion();

                if (allQuestionAnswers.containsKey(question.getName())){ //Question key already existing

                    allQuestionAnswers.get(question.getName()).add(answer);

                }else{

                    List<Answer> newAnswersList = new ArrayList<>();// New question key
                    newAnswersList.add(answer);

                    allQuestionAnswers.put(question.getName(),newAnswersList);

                }

            }

        }

        for (Map.Entry<String, List<Answer>> entry : allQuestionAnswers.entrySet()) {
            String questionName = entry.getKey();
            List<Answer> answers = entry.getValue();

            Question question = survey.getQuestionByName(questionName);

            if (question != null){

                switch (question.getType()){

                    case Paragraph -> {

                        VBox template = new VBox();
                        template.setFillWidth(true);
                        template.setSpacing(5);

                        template.setAlignment(Pos.TOP_CENTER);

                        Text questionNameText = new Text(questionName);
                        questionNameText.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR,20));

                        VBox valueContainer = new VBox();

                        for (Answer answer : answers){

                            Text answerValue = new Text(answer.getValue().toString());
                            valueContainer.getChildren().add(answerValue);

                        }

                        ScrollPane templateScroll = new ScrollPane(valueContainer);
                        templateScroll.setFitToWidth(true);
                        templateScroll.setPrefHeight(100);
                        templateScroll.setMinHeight(Region.USE_PREF_SIZE);

                        templateScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                        template.getChildren().addAll(questionNameText,templateScroll);

                        sumAnswersContainer.getChildren().add(template);

                    }
                    default -> {

                        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                        //sort the data
                        Map<Object, Long> answerCounts = answers.stream().collect(Collectors.groupingBy(Answer::getValue, Collectors.counting()));


                        answerCounts.forEach((response, count) -> pieChartData.add(new PieChart.Data(response.toString(), count)));


                        PieChart chart = new PieChart(pieChartData);
                        chart.setLegendVisible(true);
                        chart.setLegendSide(Side.RIGHT);
                        chart.setTitle(questionName);

                        sumAnswersContainer.getChildren().add(chart);

                    }


                }


            }else{
                System.err.println("Question not found by name! something went wrong");

            }

        }


    }

    @FXML
    protected void onNextButton(){
        fillInAnswer(submissionIndex+1);

    }

    @FXML
    protected void onPreviousButton(){
        fillInAnswer(submissionIndex-1);

    }

    @FXML
    protected void goToSurveyList() throws IOException {
        App.setRoot("surveylist");
    }


}
