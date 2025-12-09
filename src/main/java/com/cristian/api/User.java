package com.cristian.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private final String id;
    private final String name;
    private final String email;

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("name") String name,
                @JsonProperty("email") String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}
