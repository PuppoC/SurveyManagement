package Classes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Survey {

    private UUID id;
    private String name;
    private String desc;
    private List<Question> questions;
    private UUID creatorId;
    private Instant createdInstant;



    public void setId(UUID id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setDesc(String desc){this.desc = desc;}
    public void setQuestions(List<Question> questions){this.questions = questions;}
    public void setCreatorId(UUID creatorId){this.creatorId = creatorId;}
    public void setCreatedInstant(Instant createdInstant){this.createdInstant = createdInstant;}



    public UUID getId() {return this.id;}
    public String getName() {return this.name;}
    public String getDesc() {return this.desc;}
    public List<Question> getQuestions() {return this.questions;}
    public UUID getCreatorId(){return this.creatorId;}
    public Instant getCreatedInstant(){return this.createdInstant;}


}
