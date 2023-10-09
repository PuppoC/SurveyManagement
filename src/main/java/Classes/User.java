package Classes;

import Enums.AccessLevel;

import java.util.Map;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private AccessLevel access;


    public void setId(UUID id){this.id = id;}
    public void setUsername(String newUsername){this.username = newUsername;}
    public void setPassword(String password){this.password = password;}
    public void setAccess(AccessLevel access){this.access = access;}



    public UUID getId(){return this.id;}
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public AccessLevel getAccess(){return this.access;}

//    public Map<String,String> getAccountInfo(){return this.accountInfo;

//    public User(){
//
//        this.username = username;
//        this.accountInfo = AccountHandlerOLD.getAccountInfo(username);
//        this.access = AccessLevel.valueOf(this.accountInfo.get("access"));
//
//    }


}
