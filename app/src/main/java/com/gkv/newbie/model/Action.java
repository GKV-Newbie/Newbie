package com.gkv.newbie.model;

public class Action {

    String name;
    String stepTitle;

    public Action() {
        this("","");
    }

    public Action(String name, String stepTitle) {
        this.name = name;
        this.stepTitle = stepTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public void setStepTitle(String stepTitle) {
        this.stepTitle = stepTitle;
    }

    @Override
    public String toString() {
        return "Action{" +
                "name='" + name + '\'' +
                ", stepTitle='" + stepTitle + '\'' +
                '}';
    }
}
