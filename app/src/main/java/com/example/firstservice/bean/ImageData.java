package com.example.firstservice.bean;

import org.litepal.crud.DataSupport;

import java.sql.Date;

public class ImageData extends DataSupport {
    private int id;
    private String imagePath;
    private Date date;

    public ImageData(String imagePath, Date date) {
        this.imagePath = imagePath;
        this.date = date;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "id=" + id +
                ", imagePath='" + imagePath + '\'' +
                ", date=" + date +
                '}';
    }
}
