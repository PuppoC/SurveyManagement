package classes;

import java.util.List;
import java.util.UUID;

public class Submission {

    private UUID userId;
    private List<Answer> answers;

    public UUID getUserId() {return userId;}
    public List<Answer> getAnswers() {return answers;}

    public void setUserId(UUID userId) {this.userId = userId;}
    public void setAnswers(List<Answer> answers) {this.answers = answers;}

}

