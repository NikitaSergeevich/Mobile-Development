package com.innopolis.example.assignment1;

public class Project{

    private String name;
    private String description;
    private String author;
    private String link;
    private float rate;

    public Project(String name, String description, String author, String link)
    {
        this.name = name;
        this.description = description;
        this.author = author;
        this.link = link;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRate() {
        return Float.toString(rate);
    }
    public void setRate(float rate) {
        this.rate = rate;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}