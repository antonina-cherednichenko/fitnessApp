package com.cherednichenko.antonina.detoxdiet.detox_diet_data;

import java.io.Serializable;

/**
 * Created by tonya on 8/29/16.
 */
public class DayInfo implements Serializable{
    private String name;
    private String description;

    public DayInfo() {
        super();
    }

    public DayInfo(String name, String description) {
        this.name = name;
        this.description = description;
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
}
