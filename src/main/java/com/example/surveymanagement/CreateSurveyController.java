package com.example.surveymanagement;

import Classes.Question;
import Classes.QuestionElements;
import Classes.Survey;
import Enums.QuestionType;
import Handlers.StorageHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;

import java.io.IOException;
import java.util.*;

public class CreateSurveyController {

    @FXML VBox questionContainer;
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


//    public void setEditMode(boolean editMode){this.editMode = editMode;}

    private static void showBox(HBox hbox, boolean value){

        hbox.setManaged(value);
        hbox.setVisible(value);
        hbox.setDisable(!value);

    }

    @FXML
    public void initialize() {

        showBox(questionTemplate,false);//Hide templates
        showBox(valueTemplate,false);

        allQuestionElements = new ArrayList<QuestionElements>();

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

    public void addQuestion(Question defaultQuestion){

        QuestionElements questionElements = new QuestionElements();
        allQuestionElements.add(questionElements);

        HBox template = new HBox();
        template.setAlignment(Pos.CENTER);
        template.setPrefSize(920,deleteButtonBigSize);
        template.setPadding(new Insets(10,0,10,50)); // to take into account the delete button sticking out
        template.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));



        Button deleteQuestionButton = new Button("");
        deleteQuestionButton.setPrefHeight(deleteButtonBigSize);

        ImageView deleteQuestionButtonImageView = new ImageView(imagePath+"trash.png");
        deleteQuestionButtonImageView.setFitWidth(30);
        deleteQuestionButtonImageView.setFitHeight(30);

        deleteQuestionButton.setGraphic(deleteQuestionButtonImageView);

        deleteQuestionButton.setOnAction(event -> {
            questionContainer.getChildren().remove(template);
            allQuestionElements.remove(questionElements);
        });


        VBox questionSubBox = new VBox();

        template.getChildren().addAll(questionSubBox,deleteQuestionButton);



        HBox nameHBox = new HBox();
        nameHBox.setAlignment(Pos.CENTER);
        nameHBox.setSpacing(10);

        Text nameText = new Text("Question:");
        nameText.setWrappingWidth(200);
        nameText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
        nameText.setTextAlignment(TextAlignment.RIGHT);

        TextField nameTextField = new TextField(defaultQuestion.getName());
        nameTextField.setPrefSize(400,30);

        questionElements.setNameTextField(nameTextField);

        nameHBox.getChildren().addAll(nameText,nameTextField);



        HBox typeHBox = new HBox();
        typeHBox.setAlignment(Pos.CENTER);
        typeHBox.setSpacing(10);

        Text typeText = new Text("Type:");
        typeText.setWrappingWidth(200);
        typeText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
        typeText.setTextAlignment(TextAlignment.RIGHT);

        ComboBox<QuestionType> typeComboBox = new ComboBox<QuestionType>();
        typeComboBox.setPrefSize(400,30);

        typeComboBox.getItems().addAll(QuestionType.values());

        typeHBox.getChildren().addAll(typeText,typeComboBox);

        questionElements.setTypeComboBox(typeComboBox);




        HBox valueHBox = new HBox();
        valueHBox.setAlignment(Pos.CENTER);
        valueHBox.setSpacing(10);

        Text valueText = new Text("Values:");
        valueText.setWrappingWidth(200);
        valueText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12));
        valueText.setTextAlignment(TextAlignment.RIGHT);

        VBox valueContainer = new VBox();


        Button addValueButton = new Button("");
        addValueButton.setPrefSize(400,30);




        // CREATE VALUE TEMPLATE
        // CUSTOM EVENT, TO PASS IN DEFAULT VALUE
        class AddValueEvent extends ActionEvent {
            private String value;

            public AddValueEvent(Object source, String value) {
                super(source, null);
                this.value = value;
            }

            public String getCustomParameter() {
                return value;
            }
        }

        EventHandler<AddValueEvent> addValueEventHandler = addValueEvent -> {

            HBox valueTemplate = new HBox();

            TextArea valueTextArea = new TextArea(addValueEvent.getCustomParameter());
            valueTextArea.setPrefSize(350,40);


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



        };


        addValueButton.setOnAction(actionEvent -> {

            AddValueEvent customEvent = new AddValueEvent(this, "");// Default value is ""
            addValueEventHandler.handle(customEvent); // FUTURE ME, SEE IF CAN USE WITHOUT THIS

        });



        ImageView addValueButtonImage = new ImageView(imagePath+"plus.png");

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
        valueScrollPane.setPrefSize(400,200);
        valueScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        valueHBox.getChildren().addAll(valueText,valueScrollPane);

        // Fill in default values
        for (String value : defaultQuestion.getValues()){

            AddValueEvent customEvent = new AddValueEvent(this, value);
            addValueEventHandler.handle(customEvent);

        }

        questionSubBox.getChildren().addAll(nameHBox,typeHBox,valueHBox);





        // Listener for when value box changes question type
        typeComboBox.valueProperty().addListener(new ChangeListener<QuestionType>() {
            @Override
            public void changed(ObservableValue<? extends QuestionType> observableValue, QuestionType changedFrom, QuestionType changedTo) {

                switch (changedTo) {
                    case Paragraph -> {
                        showBox(valueHBox, false);
                        deleteQuestionButton.setPrefHeight(deleteButtonSmallSize);
                        template.setPrefHeight(deleteButtonSmallSize);
                    }
                    case MCQ, Dropdown, Scale, Checkbox -> {
                        showBox(valueHBox, true);
                        deleteQuestionButton.setPrefHeight(deleteButtonBigSize);
                        template.setPrefHeight(deleteButtonBigSize);
                    }
                    default -> System.out.println(changedTo.name() + " is not a registered question type!");
                }

            }
        });

        typeComboBox.setValue(defaultQuestion.getType());// set default option


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

        List<Question> allQuestions = new ArrayList<Question>();

        //Convert question elements to just question, in string format
        allQuestionElements.forEach(questionElements -> allQuestions.add(getQuestionFromElements(questionElements)));

        survey.setQuestions(allQuestions);

        StorageHandler.writeObjectToFile(survey,"Surveys/"+survey.getId().toString());



        Alert createdAlert = new Alert(Alert.AlertType.INFORMATION);
        createdAlert.setHeaderText(createMode ? "Survey Created Successfully!" : "Survey Edited Successfully!");
        createdAlert.showAndWait();

        System.out.println(createMode ? "Survey Created" : "Survey Edited");

        App.setRoot("surveylist");

//        App.setRoot(createMode ? "landingpage" : "surveylist");


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

        List<String> allValues = new ArrayList<String>();

        List<TextArea> allValueTextAreas = questionElements.getValueTextAreas();

        // get each textarea text, and st ore it into another list in string format
        allValueTextAreas.forEach(textArea -> allValues.add(textArea.getText()));

        question.setValues(allValues);
        return question;
    }

    @FXML protected void goToLandingPage() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to leave this page? Your survey will not be saved");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
//            App.setRoot(createMode ? "landingpage" : "surveylist");
            App.setRoot("surveylist");
        } else {
            return;
        }


    }



}
