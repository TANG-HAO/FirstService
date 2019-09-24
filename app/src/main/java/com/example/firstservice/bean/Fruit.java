package com.example.firstservice.bean;

public class Fruit {
    private String fruitName;
    private int ImageId;

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public Fruit(String fruitName, int imageId) {
        this.fruitName = fruitName;
        ImageId = imageId;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "fruitName='" + fruitName + '\'' +
                ", ImageId=" + ImageId +
                '}';
    }
}
