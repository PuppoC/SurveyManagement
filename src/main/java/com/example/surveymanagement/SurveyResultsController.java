package com.example.surveymanagement;

import classes.*;
import enums.QuestionType;
import handlers.StorageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SurveyResultsController {

    private Survey survey;
    private int submissionIndex = 0;


    public void setSurvey(Survey survey) {
        this.survey = survey;
    }


    @FXML
    Button backButton;
    @FXML
    Button nextButton;
    @FXML
    Button prevButton;


    @FXML
    Text surveyTitleText;
    @FXML
    Text surveyDescriptionText;
    @FXML
    Text userInfoText;
    @FXML
    VBox indivAnswersContainer;
    @FXML
    VBox sumAnswersContainer;

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

        if ((index < 0) || (index > (survey.getSubmissions().size() - 1))) {// Index out of bounds check
            return;
        }

        Submission submission = survey.getSubmissions().get(index);

        indivAnswersContainer.getChildren().clear();

        User user = StorageHandler.readObjectFromFile("Users/" + submission.getUserId(), User.class);


        userInfoText.setText(String.format("[%s/%s] Submitted by: %s", index + 1, survey.getSubmissions().size(), user.getId() == null ? "Deleted User" : user.getUsername()));


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

    private double getPieTotal(PieChart pieChart) {
        return pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum();
    }


    @FXML
    void initialize() {

        backButton.setOnAction(actionEvent -> onBackButton());

        nextButton.setOnAction(actionEvent -> fillInAnswer(submissionIndex + 1));
        prevButton.setOnAction(actionEvent -> fillInAnswer(submissionIndex - 1));

        surveyTitleText.setText(survey.getName());
        surveyDescriptionText.setText(survey.getDesc());

        //INIT INDIVIDUAL SECTION
        if (!survey.getSubmissions().isEmpty()) {
            fillInAnswer(submissionIndex);
        } else {
            userInfoText.setText("No Submissions!");
        }


        //INIT SUMMARY SECTION

        //Group submissions by answers
        Map<Question, List<Answer>> allQuestionAnswers = new HashMap<>();

        for (Submission submission : survey.getSubmissions()) {

            for (Answer answer : submission.getAnswers()) {

                if (answer.getValue().toString().isEmpty()) { // ignore empty answers
                    continue;
                }

                Question question = answer.getQuestion();

                List<Answer> storedAnswers = null;

                for (Map.Entry<Question, List<Answer>> entry : allQuestionAnswers.entrySet()) {
                    Question storedQuestion = entry.getKey();

                    if (storedQuestion.getId().equals(question.getId())) {
                        storedAnswers = entry.getValue();
                        break;
                    }

                }

                if (storedAnswers != null) { //Question key already existing

                    storedAnswers.add(answer);

                } else {
                    List<Answer> newAnswersList = new ArrayList<>();// New question key
                    newAnswersList.add(answer);

                    allQuestionAnswers.put(question, newAnswersList);

                }

            }

        }

        for (Map.Entry<Question, List<Answer>> entry : allQuestionAnswers.entrySet()) {

            Question question = entry.getKey();
            List<Answer> answers = entry.getValue();

            if (Objects.requireNonNull(question.getType()) == QuestionType.Paragraph) {
                VBox template = new VBox();
                template.setFillWidth(true);
                template.setSpacing(5);

                template.setAlignment(Pos.TOP_CENTER);

                Text questionNameText = new Text(question.getName());
                questionNameText.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 16));

                VBox valueContainer = new VBox();
                valueContainer.setAlignment(Pos.TOP_CENTER);

                for (Answer answer : answers) {

                    Text answerValue = new Text(answer.getValue().toString());
                    valueContainer.getChildren().add(answerValue);

                }

                ScrollPane templateScroll = new ScrollPane(valueContainer);
                templateScroll.setFitToWidth(true);
                templateScroll.setPrefHeight(100);
                templateScroll.setMinHeight(Region.USE_PREF_SIZE);

                templateScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                template.getChildren().addAll(questionNameText, templateScroll);

                sumAnswersContainer.getChildren().add(template);

            } else {
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                //sort the data
                Map<Object, Long> answerCounts = answers.stream()
                        .flatMap(answer -> {
                            if (answer.getValue() instanceof List) {
                                return ((List<?>) answer.getValue()).stream();
                            } else {
                                return Stream.of(answer.getValue());
                            }
                        })
                        .collect(Collectors.groupingBy(
                                value -> value,
                                Collectors.counting()
                        ));


                answerCounts.forEach((response, count) -> pieChartData.add(new PieChart.Data(response.toString(), count)));

                PieChart chart = new PieChart(pieChartData);
                chart.setTitle(question.getName());
                chart.setLabelLineLength(10);
                chart.setLegendVisible(false); // Hide the legend
                chart.getData().forEach(data -> {
                    String percentage = String.format("%.1f%%", (data.getPieValue() / getPieTotal(chart)) * 100);
                    data.nameProperty().bind(Bindings.concat(data.getName(), " ", percentage));
                });

                sumAnswersContainer.getChildren().add(chart);
            }
        }

    }

    private void onBackButton() {
        try {
            App.setRoot("surveylist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
