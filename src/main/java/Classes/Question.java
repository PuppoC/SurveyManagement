package Classes;

import Enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String name;
    private QuestionType type;
    private List<String> values;




    public String getName(){return this.name;}
    public QuestionType getType(){return this.type;}
    public List<String> getValues(){return this.values;}

    public void setName(String name){this.name = name;}
    public void setType(QuestionType type){this.type = type;}
    public void setValues(List<String> values){this.values = values;}

    public Question(){

        this.name = "";
        this.type = QuestionType.Paragraph;
        this.values = new ArrayList<>();

    }


}
