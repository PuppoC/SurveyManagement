package com.example.surveymanagement;

import Classes.Question;
import Classes.QuestionElements;
import Classes.Survey;
import Enums.QuestionType;
import Handlers.StorageHandler;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class CreateSurveyController {

    @FXML VBox questionContainer;
    @FXML ScrollPane questionScrollPane;
    @FXML HBox questionTemplate;
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

        showNode(questionTemplate,false);//Hide templates
        showNode(valueTemplate,false);

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


    private final int deleteButtonBigSize = 250;
    private final int deleteButtonSmallSize = 60;
    private void addValue(String value, VBox valueContainer, QuestionElements questionElements){

        HBox valueTemplate = new HBox();

        TextArea valueTextArea = new TextArea(value);
        valueTextArea.setPrefSize(350,40);
        valueTextArea.setPromptText("Option");

        Button valueDeleteButton = new Button("");
        valueDeleteButton.setPrefSize(50,40);

        ImageView valueDeleteButtonImageView = new ImageView(imagePath+"close.png");
        valueDeleteButtonImageView.setFitWidth(20);
        valueDeleteButtonImageView.setFitHeight(20);

        valueDeleteButton.setGraphic(valueDeleteButtonImageView);

        valueDeleteButton.setOnAction(deleteValueEvent -> {
            valueContainer.getChildren().remove(valueTemplate);
            questionElements.getValueTextAreas().remove(valueTextArea);

        });


        valueTemplate.getChildren().addAll(valueTextArea,valueDeleteButton);

        valueContainer.getChildren().add(valueContainer.getChildren().size()-1, valueTemplate); // place in 2nd last order, that's why it's -1

        questionElements.getValueTextAreas().add(valueTextArea);

    }

    public void addQuestion(Question defaultQuestion){

        QuestionElements questionElements = new QuestionElements();
        allQuestionElements.add(questionElements);

        HBox template = new HBox();
        template.setAlignment(Pos.CENTER);
        template.setPrefSize(920,deleteButtonBigSize);
//        template.setPadding(new Insets(10,0,10,0));
        template.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));



        Button deleteQuestionButton = new Button("");
        deleteQuestionButton.setPrefHeight(deleteButtonBigSize);

        ImageView deleteQuestionButtonImageView = new ImageView(imagePath+"trash.png");
        deleteQuestionButtonImageView.setFitWidth(30);
        deleteQuestionButtonImageView.setFitHeight(30);

        deleteQuestionButton.setGraphic(deleteQuestionButtonImageView);


        VBox questionSubBox = new VBox();

        template.getChildren().addAll(questionSubBox,deleteQuestionButton);



//        HBox nameHBox = new HBox();
//        nameHBox.setAlignment(Pos.CENTER);
//        nameHBox.setSpacing(10);

//        Text nameText = new Text("Question:");
//        nameText.setWrappingWidth(200);
//        nameText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
//        nameText.setTextAlignment(TextAlignment.RIGHT);

        TextField nameTextField = new TextField(defaultQuestion.getName());
        nameTextField.setPrefSize(400,30);
        nameTextField.setPromptText("Question");

        questionElements.setNameTextField(nameTextField);

//        nameHBox.getChildren().addAll(nameText,nameTextField);



//        HBox typeHBox = new HBox();
//        typeHBox.setAlignment(Pos.CENTER);
//        typeHBox.setSpacing(10);

//        Text typeText = new Text("Type:");
//        typeText.setWrappingWidth(200);
//        typeText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
//        typeText.setTextAlignment(TextAlignment.RIGHT);

        ComboBox<QuestionType> typeComboBox = new ComboBox<>();
        typeComboBox.setPrefSize(400,30);
        typeComboBox.setPromptText("Question Type");

        typeComboBox.getItems().addAll(QuestionType.values());

//        typeHBox.getChildren().addAll(typeText,typeComboBox);

        questionElements.setTypeComboBox(typeComboBox);




//        HBox valueHBox = new HBox();
//        valueHBox.setAlignment(Pos.CENTER);
//        valueHBox.setSpacing(10);
//
//        Text valueText = new Text("Values:");
//        valueText.setWrappingWidth(200);
//        valueText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
//        valueText.setTextAlignment(TextAlignment.RIGHT);

        VBox valueContainer = new VBox();


        Button addValueButton = new Button("");
        addValueButton.setPrefSize(400,30);


        addValueButton.setOnAction(event -> addValue("", valueContainer, questionElements));

//        addValueButton.setOnAction(actionEvent -> {
//
//            addValue("",valueContainer,questionElements);
//
//        });



        ImageView addValueButtonImage = new ImageView(imagePath+"choice.png");

        addValueButtonImage.setFitHeight(20);
        addValueButtonImage.setFitWidth(20);

        Text addValueButtonText = new Text("Add Value");



        HBox addValueButtonHBox = new HBox();
        addValueButtonHBox.setSpacing(10);
        addValueButtonHBox.setAlignment(Pos.CENTER);

        addValueButtonHBox.getChildren().addAll(addValueButtonImage,addValueButtonText);

        addValueButton.setGraphic(addValueButtonHBox);

        valueContainer.getChildren().add(addValueButton);



        ScrollPane valueScrollPane = new ScrollPane(valueContainer);
        valueScrollPane.setPrefSize(400,200);// fixed size for values
        valueScrollPane.setMinSize(400,200);
        valueScrollPane.setMaxSize(400,200);
        valueScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


//        valueHBox.getChildren().addAll(valueText,valueScrollPane);

        // Fill in default values
        for (String value : defaultQuestion.getValues()){

            addValue(value,valueContainer,questionElements);

        }

//        questionSubBox.getChildren().addAll(nameHBox,typeHBox,valueHBox);
        questionSubBox.getChildren().addAll(nameTextField,typeComboBox,valueScrollPane);





        // Listener for when value box changes question type
        ChangeListener<QuestionType> typeListener = (observableValue, changedFrom, changedTo) -> {
            switch (changedTo) {
                case Paragraph -> {
                    showNode(valueScrollPane, false);
                    deleteQuestionButton.setPrefHeight(deleteButtonSmallSize);
                    template.setPrefHeight(deleteButtonSmallSize);
                }
                case MCQ, Dropdown, Scale, Checkbox -> {
                    showNode(valueScrollPane, true);
                    deleteQuestionButton.setPrefHeight(deleteButtonBigSize);
                    template.setPrefHeight(deleteButtonBigSize);
                }
                default -> System.err.println(changedTo.name() + " is not a registered question type!");
            }
        };
        typeComboBox.valueProperty().addListener(typeListener);


        typeComboBox.setValue(defaultQuestion.getType());// set default type



        deleteQuestionButton.setOnAction(event -> {
            typeComboBox.valueProperty().removeListener(typeListener);
            questionContainer.getChildren().remove(template);
            allQuestionElements.remove(questionElements);
        });


        questionContainer.getChildren().add(questionContainer.getChildren().size()-1,template); // place in 2nd last order, that's why it's -1

    }

    @FXML protected void onAddQuestionButton(){

        addQuestion(new Question());

    }


    @FXML protected void onSubmitButton() throws IOException {

        // FOR TESTING
//        for (QuestionElements questionElements : allQuestionElements){
//
//            System.out.println("Name: " + questionElements.getNameTextField());
//            System.out.println("Type: " + questionElements.getTypeComboBox());
//            System.out.println("Values: " + questionElements.getValueTextAreas());
//
//        }

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


//        for (Question question : allQuestions){
//
//            System.out.println("Name: " + question.getName());
//            System.out.println("Type: " + question.getType().toString());
//            System.out.println("Values: " + question.getValues());
//
//        }

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

    @FXML protected void goToLandingPage() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to leave this page? Your survey will not be saved");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            App.setRoot("surveylist");
        }

    }

}
