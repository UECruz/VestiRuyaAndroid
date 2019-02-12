package com.java1.fullsail.vestiruyaalpha.activity.model;

import java.io.Serializable;

public class Customize implements Serializable{
    private String image;
    private String name;
    public boolean isSelected;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
