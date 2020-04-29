package com.gkv.newbie.model;

import java.io.Serializable;

public class Step implements Serializable {

    String title;
    String description;

    public Step() {
        this("","");
    }

    public Step(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Step{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
