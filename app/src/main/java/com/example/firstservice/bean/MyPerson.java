package com.example.firstservice.bean;

import android.media.Image;

public class MyPerson {
    private int id;
    private String persoName;
    private int personImageId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersoName() {
        return persoName;
    }

    public void setPersoName(String persoName) {
        this.persoName = persoName;
    }

    public int getPersonImageId() {
        return personImageId;
    }

    public void setPersonImageId(int personImageId) {
        this.personImageId = personImageId;
    }

    public MyPerson(String persoName, int personImageId) {
        this.persoName = persoName;
        this.personImageId = personImageId;
    }

    @Override
    public String toString() {
        return "MyPerson{" +
                "id=" + id +
                ", persoName='" + persoName + '\'' +
                ", personImageId=" + personImageId +
                '}';
    }

    public MyPerson() {
    }
}
