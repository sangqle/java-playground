package com.sangqle.sample.synchronous;

public class User {
    private int id;
    private String username;

    public User() {}

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    @Override
    public String toString() {
        return String.format("id: %s, username: %s", id, username);
    }
}
