package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.PasswordManager;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

public class User implements Principal {

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private UUID id;
    private String name;
    private String password;
    private List<String> roles;

    public User(){}

    public User(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }


    @JsonCreator
    public User(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {
        this.id = UUID.randomUUID();
        this.name = username;
        this.password = PasswordManager.generateFromPassword(password);
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getName() {
        return this.name;
    }
    public List<String> getRoles() {
        return this.roles;
    }


    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
