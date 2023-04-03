package com.sangqle.sample.hibernate;

public class Animal {

    private long id;
    private String name;
    private int value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String rs = String.format("Animal[name=%s, value=%s]", name, value);
        return rs;
    }
}
