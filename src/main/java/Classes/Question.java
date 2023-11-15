package Classes;

import Enums.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {

    private String name;
    private UUID id;
    private QuestionType type;
    private List<String> values;


    public String getName(){return this.name;}
    public UUID getId(){return this.id;}
    public QuestionType getType(){return this.type;}
    public List<String> getValues(){return this.values;}

    public void setName(String name){this.name = name;}
    public void setId(UUID id){this.id = id;}
    public void setType(QuestionType type){this.type = type;}
    public void setValues(List<String> values){this.values = values;}

    public Question(){

        this.name = "";
        this.type = QuestionType.Paragraph;
        this.values = new ArrayList<>();

    }


}
