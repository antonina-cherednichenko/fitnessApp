package com.cherednichenko.antonina.detoxdiet.detox_diet_data;

import java.io.Serializable;

/**
 * Created by tonya on 8/29/16.
 */
public class DayInfo implements Serializable{
    private String name;
    private String description;
    private String photo;

    //now it is used as mode
    private int onlyPhoto;

    public DayInfo() {
        super();
    }

    public DayInfo(String name, String description, String photo, int onlyPhoto) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.onlyPhoto = onlyPhoto;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getOnlyPhoto() {
        return onlyPhoto;
    }

    public void setOnlyPhoto(int onlyPhoto) {
        this.onlyPhoto = onlyPhoto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
