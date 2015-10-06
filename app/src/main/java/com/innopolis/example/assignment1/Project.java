package com.innopolis.example.assignment1;

/**
 * Created by user on 23.09.2015.
 */
public class Project{

    private String name;
    private String description;
    private String author;
    private int image;
    private float rate;

    public Project(String name, String description, int image)
    {
        this.name = name;
        this.description = description;
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String description) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public String getRate() {
        return Float.toString(rate);
    }
    public void setRate(float value) {
        rate = value;
    }
}