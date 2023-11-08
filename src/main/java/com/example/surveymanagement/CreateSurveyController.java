package com.example.surveymanagement;

import Classes.Question;
import Classes.QuestionElements;
import Classes.Survey;
import Enums.QuestionType;
import Handlers.StorageHandler;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class CreateSurveyController {

    @FXML VBox questionContainer;
    @FXML ScrollPane questionScrollPane;
    @FXML GridPane questionTemplate;
    @FXML HBox valueTemplate;
    @FXML TextField nameTextField;
    @FXML TextArea descTextArea;
    @FXML Text titleText;
    @FXML Button submitButton;

    private List<QuestionElements> allQuestionElements;
    private static final String imagePath = "file:src/main/resources/com/example/surveymanagement/Images/";
    private Survey defaultSurvey;
    private boolean createMode = true; // CREATE MODE IS OPPOSITE OF EDIT MODE


    private static void showNode(Node node, boolean value){

        node.setManaged(value);
        node.setVisible(value);
        node.setDisable(!value);

    }


    @FXML
    public void initialize() {

        questionContainer.getChildren().remove(questionTemplate); // Instead of hide template, remove it completely

//        showNode(questionTemplate,false);//Hide templates
//        showNode(valueTemplate,false);

        allQuestionElements = new ArrayList<>();


        if (defaultSurvey != null){

            nameTextField.setText(defaultSurvey.getName());
            descTextArea.setText(defaultSurvey.getDesc());

            titleText.setText("Survey Editor");

            submitButton.setText("Submit Edit");

            createMode = false;

        }
    }

    public void setDefaultSurvey(Survey defaultSurvey){this.defaultSurvey = defaultSurvey;}


    private final int bigQuestionHeight = 250;
    private final int smallQuestionHeight = 60;

    private final int sideButtonWidth = 50;// CANNOT GO TO 40, value reorder button stack, 40 is too small for the button, will artificially make bigger

    private void addValue(String value, VBox valueContainer, QuestionElements questionElements){

        HBox valueTemplate = new HBox();


        TextArea valueTextArea = new TextArea(value);

        valueTextArea.setPrefSize(9999,sideButtonWidth);

        valueTextArea.setPromptText("Option");

        Button valueDeleteButton = new Button("");
        valueDeleteButton.setPrefSize(sideButtonWidth, sideButtonWidth);
        valueDeleteButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        valueDeleteButton.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        ImageView valueDeleteButtonImageView = new ImageView(imagePath+"close.png");
        valueDeleteButtonImageView.setFitWidth(20);
        valueDeleteButtonImageView.setFitHeight(20);

        valueDeleteButton.setGraphic(valueDeleteButtonImageView);



        VBox reorderVBoxSmall = new VBox();
        reorderVBoxSmall.setAlignment(Pos.CENTER_RIGHT);
        reorderVBoxSmall.setPrefWidth(sideButtonWidth);
        reorderVBoxSmall.setMinWidth(Region.USE_PREF_SIZE);

        ImageView upArrowImageViewSmall = new ImageView(imagePath+"up-arrow.png");
        upArrowImageViewSmall.setFitWidth(5);
        upArrowImageViewSmall.setFitHeight(5);


        ImageView downArrowImageViewSmall = new ImageView(imagePath+"up-arrow.png");
        downArrowImageViewSmall.setFitWidth(5);
        downArrowImageViewSmall.setFitHeight(5);


        Button upButtonSmall = new Button("");
        upButtonSmall.setGraphic(upArrowImageViewSmall);
        upButtonSmall.setPrefSize(sideButtonWidth, (double) sideButtonWidth /2);


        Button downButtonSmall = new Button("");
        downButtonSmall.setRotate(180);
        downButtonSmall.setGraphic(downArrowImageViewSmall);
        downButtonSmall.setPrefSize(sideButtonWidth, (double) sideButtonWidth /2);


        EventHandler<ActionEvent> reorderUpHandler = event -> moveNodeUp(valueTemplate, valueContainer, questionElements.getValueTextAreas());
        EventHandler<ActionEvent> reorderDownHandler = event -> moveNodeDown(valueTemplate, valueContainer, questionElements.getValueTextAreas());

        upButtonSmall.setOnAction(reorderUpHandler);
        downButtonSmall.setOnAction(reorderDownHandler);



        valueDeleteButton.setOnAction(deleteValueEvent -> {
            valueContainer.getChildren().remove(valueTemplate);

            upButtonSmall.setOnAction(null);
            downButtonSmall.setOnAction(null);

            questionElements.getValueTextAreas().remove(valueTextArea);

        });


        reorderVBoxSmall.getChildren().addAll(upButtonSmall,downButtonSmall);

        valueTemplate.getChildren().addAll(reorderVBoxSmall,valueTextArea,valueDeleteButton);

        valueContainer.getChildren().add(valueContainer.getChildren().size(), valueTemplate);

        questionElements.getValueTextAreas().add(valueTextArea);

    }


    public <T> void moveNodeUp(Node node, VBox container, List<T> allElements) {

        int currentIndex = container.getChildren().indexOf(node);
        int newIndex = currentIndex - 1;

        if (currentIndex > 0) {
            container.getChildren().remove(currentIndex);
            container.getChildren().add(newIndex, node);
            Collections.swap(allElements, currentIndex, newIndex);
        }
    }

    public <T> void moveNodeDown(Node node, VBox container, List<T> allElements) {

        int currentIndex = container.getChildren().indexOf(node);
        int newLayoutIndex = currentIndex + 1;

        if (currentIndex >= 0 && currentIndex < (container.getChildren().size() - 1)) {
            container.getChildren().remove(currentIndex);
            container.getChildren().add(newLayoutIndex, node);
            Collections.swap(allElements, currentIndex, newLayoutIndex);
        }
    }


    public void addQuestion(Question defaultQuestion){

        QuestionElements questionElements = new QuestionElements();
        allQuestionElements.add(questionElements);

        GridPane template = new GridPane();
        template.setAlignment(Pos.CENTER);
        template.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));


        Button deleteQuestionButton = new Button("");
        deleteQuestionButton.setPrefHeight(bigQuestionHeight);

        ImageView deleteQuestionButtonImageView = new ImageView(imagePath+"trash.png");
        deleteQuestionButtonImageView.setFitWidth(30);
        deleteQuestionButtonImageView.setFitHeight(30);

        deleteQuestionButton.setGraphic(deleteQuestionButtonImageView);


        VBox reorderVBoxBig = new VBox();
        reorderVBoxBig.setAlignment(Pos.CENTER_RIGHT);

        ImageView upArrowImageViewBig = new ImageView(imagePath+"up-arrow.png");
        upArrowImageViewBig.setFitWidth(10);
        upArrowImageViewBig.setFitHeight(10);

        ImageView downArrowImageViewBig = new ImageView(imagePath+"up-arrow.png");
        downArrowImageViewBig.setFitWidth(10);
        downArrowImageViewBig.setFitHeight(10);


        Button upButtonBig = new Button("");
        upButtonBig.setGraphic(upArrowImageViewBig);
        upButtonBig.setPrefSize(sideButtonWidth,(double) bigQuestionHeight /2);

        Button downButtonBig = new Button("");
        downButtonBig.setRotate(180);
        downButtonBig.setGraphic(downArrowImageViewBig);
        downButtonBig.setPrefSize(sideButtonWidth,(double) bigQuestionHeight /2);

        reorderVBoxBig.getChildren().addAll(upButtonBig,downButtonBig);



        HBox reorderVBoxSmall = new HBox();
        reorderVBoxSmall.setAlignment(Pos.CENTER_RIGHT);

        ImageView upArrowImageViewSmall = new ImageView(imagePath+"up-arrow.png");
        upArrowImageViewSmall.setFitWidth(5);
        upArrowImageViewSmall.setFitHeight(5);


        ImageView downArrowImageViewSmall = new ImageView(imagePath+"up-arrow.png");
        downArrowImageViewSmall.setFitWidth(5);
        downArrowImageViewSmall.setFitHeight(5);


        Button upButtonSmall = new Button("");
        upButtonSmall.setGraphic(upArrowImageViewSmall);
        upButtonSmall.setPrefSize((double) sideButtonWidth /2,smallQuestionHeight);


        Button downButtonSmall = new Button("");
        downButtonSmall.setRotate(180);
        downButtonSmall.setGraphic(downArrowImageViewSmall);
        downButtonSmall.setPrefSize((double) sideButtonWidth /2,smallQuestionHeight);


        reorderVBoxSmall.getChildren().addAll(upButtonSmall,downButtonSmall);




        VBox questionSubBox = new VBox();


        ColumnConstraints col1 = new ColumnConstraints(); // Width of the first column
        col1.setPercentWidth(10);

        ColumnConstraints col2 = new ColumnConstraints(); // Width of the second column
        col2.setPercentWidth(80);

        ColumnConstraints col3 = new ColumnConstraints(); // Width of the second column
        col3.setPercentWidth(10);

        RowConstraints row1 = new RowConstraints(50); // Height of the first row

        template.getColumnConstraints().addAll(col1,col2,col3);
        template.getRowConstraints().addAll(row1);

        GridPane.setConstraints(reorderVBoxBig,0,0);
        GridPane.setConstraints(reorderVBoxSmall,0,0);
        GridPane.setConstraints(questionSubBox,1,0);
        GridPane.setConstraints(deleteQuestionButton,2,0);


        EventHandler<ActionEvent> reorderUpHandler = event -> moveNodeUp(template, questionContainer, allQuestionElements);
        EventHandler<ActionEvent> reorderDownHandler = event -> moveNodeDown(template, questionContainer, allQuestionElements);

        upButtonSmall.setOnAction(reorderUpHandler);
        upButtonBig.setOnAction(reorderUpHandler);

        downButtonSmall.setOnAction(reorderDownHandler);
        downButtonBig.setOnAction(reorderDownHandler);


        template.getChildren().addAll(reorderVBoxBig,reorderVBoxSmall,questionSubBox,deleteQuestionButton);



        TextField nameTextField = new TextField(defaultQuestion.getName());
        nameTextField.setPrefSize(9999,30);
        nameTextField.setPromptText("Question");

        questionElements.setNameTextField(nameTextField);




        ComboBox<QuestionType> typeComboBox = new ComboBox<>();
        typeComboBox.setPrefSize(9999,30);

        typeComboBox.setPromptText("Question Type");

        typeComboBox.getItems().addAll(QuestionType.values());

        questionElements.setTypeComboBox(typeComboBox);



        VBox valuesSectionContainer = new VBox();

        VBox valueContainer = new VBox();



        Button addValueButton = new Button("Add Value");
        addValueButton.setPrefSize(9999,30);

        addValueButton.setOnAction(event -> addValue("", valueContainer, questionElements));


        ImageView addValueButtonImage = new ImageView(imagePath+"choice.png");
        addValueButton.setGraphic(addValueButtonImage);
        addValueButtonImage.setFitHeight(20);
        addValueButtonImage.setFitWidth(20);


        valuesSectionContainer.getChildren().addAll(valueContainer,addValueButton);



        ScrollPane valueScrollPane = new ScrollPane();
        valueScrollPane.setContent(valuesSectionContainer);
//        valueScrollPane.setPrefSize(9999,200);// fixed size for values
        valueScrollPane.setFitToWidth(true);
        valueScrollPane.setMinHeight(200);
        valueScrollPane.setMaxHeight(200);
        valueScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        // Fill in default values
        for (String value : defaultQuestion.getValues()){

            addValue(value,valueContainer,questionElements);

        }

        questionSubBox.getChildren().addAll(nameTextField,typeComboBox,valueScrollPane);



        // Listener for when value box changes question type
        ChangeListener<QuestionType> typeListener = (observableValue, changedFrom, changedTo) -> {
            switch (changedTo) {
                case Paragraph, Numbers -> {
                    showNode(valueScrollPane, false);
                    showNode(reorderVBoxBig,false);
                    showNode(reorderVBoxSmall,true);
                    deleteQuestionButton.setPrefHeight(smallQuestionHeight);

                    row1.setPrefHeight(smallQuestionHeight);
                }
                case MCQ, Dropdown, Scale, Checkbox -> {
                    showNode(valueScrollPane, true);
                    showNode(reorderVBoxBig,true);
                    showNode(reorderVBoxSmall,false);
                    deleteQuestionButton.setPrefHeight(bigQuestionHeight);

                    row1.setPrefHeight(bigQuestionHeight);
                }
                default -> System.err.println(changedTo.name() + " is not a registered question type!");
            }
        };
        typeComboBox.valueProperty().addListener(typeListener);


        typeComboBox.setValue(defaultQuestion.getType());// set default type


        deleteQuestionButton.setOnAction(event -> {
            typeComboBox.valueProperty().removeListener(typeListener);
            questionContainer.getChildren().remove(template);

            upButtonSmall.setOnAction(null);
            upButtonBig.setOnAction(null);

            downButtonSmall.setOnAction(null);
            downButtonBig.setOnAction(null);

            addValueButton.setOnAction(null);

            allQuestionElements.remove(questionElements);
        });


        questionContainer.getChildren().add(questionContainer.getChildren().size(),template); // place in 2nd last order, that's why it's -1

    }

    @FXML protected void onAddQuestionButton(){

        addQuestion(new Question());

    }


    @FXML protected void onSubmitButton() throws IOException {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setHeaderText(createMode ? "Confirm to create survey?" : "Confirm survey edits?");


        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if (confirmResult.isPresent() && (confirmResult.get() == ButtonType.CANCEL)){
            System.out.println(createMode ? "Survey Creation Abandoned" : "Survey edit abandoned");
            return;
        }

        Survey survey = new Survey();
        survey.setId(createMode ? UUID.randomUUID() : defaultSurvey.getId());
        survey.setName(nameTextField.getText());
        survey.setDesc(descTextArea.getText());
        survey.setCreatorId(Objects.requireNonNull(App.getSessionUser()).getId());
        survey.setCreatedInstant(createMode ? Instant.now() : defaultSurvey.getCreatedInstant());

        List<Question> allQuestions = new ArrayList<>();

        //Convert question elements to just question, in string format
        allQuestionElements.forEach(questionElements -> allQuestions.add(getQuestionFromElements(questionElements)));

        survey.setQuestions(allQuestions);

        StorageHandler.writeObjectToFile(survey,"Surveys/"+survey.getId().toString());



        Alert createdAlert = new Alert(Alert.AlertType.INFORMATION);
        createdAlert.setHeaderText(createMode ? "Survey Created Successfully!" : "Survey Edited Successfully!");
        createdAlert.showAndWait();

        System.out.println(createMode ? "Survey Created" : "Survey Edited");

        App.setRoot("surveylist");


    }

    // Change question elements class format to question class format
    private static Question getQuestionFromElements(QuestionElements questionElements) {
        Question question = new Question();
        question.setName(questionElements.getNameTextField().getText());
        question.setType(questionElements.getTypeComboBox().getValue());

        List<String> allValues = new ArrayList<>();

        List<TextArea> allValueTextAreas = questionElements.getValueTextAreas();

        // get each textarea text, and store it into another list in string format
        allValueTextAreas.forEach(textArea -> allValues.add(textArea.getText()));

        question.setValues(allValues);
        return question;
    }

    @FXML protected void goToSurveyList() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to leave this page? Your survey will not be saved");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            App.setRoot("surveylist");
        }

    }

}
