package Classes;

import Enums.QuestionType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class QuestionElements {

    private TextField nameTextField;
    private ComboBox<QuestionType> typeComboBox;
    private List<TextArea> valueTextAreas;

    public QuestionElements() {

        valueTextAreas = new ArrayList<>();

    }

    public void setNameTextField(TextField nameTextField){this.nameTextField = nameTextField;}
    public TextField getNameTextField(){return this.nameTextField;}


    public void setTypeComboBox(ComboBox<QuestionType> typeComboBox){this.typeComboBox = typeComboBox;}
    public ComboBox<QuestionType> getTypeComboBox(){return this.typeComboBox;}


    public List<TextArea> getValueTextAreas(){return this.valueTextAreas;}


}
