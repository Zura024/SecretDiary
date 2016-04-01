package com.example.lakhs.secretdiary.model;


public class Setting {

    private int title;
    private int description;
    private int action;

    public Setting(int title,int description,int action){
        this.title = title;
        this.description = description;
        this.action = action;
    }

    public int getDescription() {
        return description;
    }
    public int getTitle() {
        return title;
    }
    public int getAction(){
        return action;
    }
}
