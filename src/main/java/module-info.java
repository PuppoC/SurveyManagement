module com.example.surveymanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires jbcrypt;



    opens com.example.surveymanagement to javafx.fxml;
    exports com.example.surveymanagement;
    exports Enums;
    opens Enums to javafx.fxml;
    exports Handlers;
    opens Handlers to javafx.fxml;
    opens Classes to com.google.gson, javafx.fxml;
    exports Classes;


}