package com.example.surveymanagement;

import Classes.User;
import Enums.AccessLevel;
import Handlers.PasswordEncoderDecoder;
import Handlers.StorageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class LoginSignUpController {

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordField;
    @FXML Text errorMsgText;

    private static final String usersFolderName = "Users";
    private static Map<String,UUID> usernamesAndUUIDs;

    @FXML
    protected void onSignUpButton(){

        String signupUsername = usernameTextField.getText();
        String signupPassword = passwordField.getText();

        // Username Check
        // Length check
        if (signupUsername.length() < 5){

            errorMsgText.setText("Username must be at least 5 characters long");
            return;

        }

        // Space check
        if (signupUsername.contains(" ")){

            errorMsgText.setText("Username cannot have spaces");
            return;

        }

        // Existence check
        if (usernamesAndUUIDs.containsKey(signupUsername)){

            errorMsgText.setText("Username already exists");
            return;

        }


        // Password check
        // Length check
        if (signupPassword.length() < 5){

            errorMsgText.setText("Password must be at least 5 characters long");
            return;

        }

        // Space check
        if (signupPassword.contains(" ")){

            errorMsgText.setText("Password cannot have spaces");
            return;

        }

        System.out.println("Valid account details, creating user");
        errorMsgText.setText("Creating Account");

        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setUsername(signupUsername);
        newUser.setPassword(PasswordEncoderDecoder.encodePassword(signupPassword));
        newUser.setAccess(AccessLevel.NORMAL);

        StorageHandler.writeObjectToFile(newUser,usersFolderName+"/"+newUser.getId().toString());

        goToLandingPage(newUser);

    }

    @FXML
    protected void onLoginButton(){

        String loginUsername = usernameTextField.getText();
        String loginPassword = passwordField.getText();

        UUID userUUID = usernamesAndUUIDs.get(loginUsername);

        if (userUUID == null){
            System.out.println("Username doesn't exist");
            errorMsgText.setText("Username or password is incorrect");
            return;

        }

        User user = StorageHandler.readObjectFromFile(usersFolderName+"/"+userUUID.toString(),User.class);

        // Password for this username is incorrect
        if (!PasswordEncoderDecoder.verifyPassword(loginPassword,user.getPassword())){
            System.out.println("Wrong password");
            errorMsgText.setText("Username or password is incorrect");
            return;

        }

        System.out.println("Valid account details, logging in");
        errorMsgText.setText("Valid credentials, logging in");

        goToLandingPage(user);

    }


    private void goToLandingPage(User user){

        try{

            App.setSessionUser(user);
            App.setRoot("landingpage");

        } catch(IOException e){

            System.out.println("Unable to go to landing page: " + e.getMessage());

        }

    }


    @FXML
    protected void goToLogin() throws IOException {App.setRoot("login");}
    @FXML
    protected void goToSignUp() throws IOException {App.setRoot("signup");}


    @FXML
    public void initialize() {

        usernamesAndUUIDs = new HashMap<>();

        List<User> allUsers = StorageHandler.getEachObjectInFolder(usersFolderName,User.class);

        for(User user : allUsers){

            usernamesAndUUIDs.put(user.getUsername(),user.getId());

        }

    }

}