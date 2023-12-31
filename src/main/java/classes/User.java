package classes;

import enums.AccessLevel;

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

}
