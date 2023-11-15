module com.example.surveymanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.gson;
    requires jbcrypt;



    opens com.example.surveymanagement to javafx.fxml;
    exports com.example.surveymanagement;
    exports enums;
    opens enums to javafx.fxml;
    exports handlers;
    opens handlers to javafx.fxml;
    opens classes to com.google.gson, javafx.fxml;
    exports classes;


}