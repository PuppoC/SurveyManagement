package Classes;

public class Answer {

//    private UUID surveyId;
    private Question question;
    private Object value;


//    public UUID getSurveyId() {return surveyId;}
    public Object getValue() {return value;}
    public Question getQuestion(){return question;}

//    public void setSurveyId(UUID surveyId) {this.surveyId = surveyId;}
    public void setQuestion(Question question) {this.question = question;}
    public void setValue(Object value) {this.value = value;}


}
