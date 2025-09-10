package com.endre.demo.domain;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String email;

    public User() { }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
