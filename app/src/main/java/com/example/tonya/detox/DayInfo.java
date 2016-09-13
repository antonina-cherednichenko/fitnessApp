package com.example.tonya.detox;

import java.io.Serializable;

/**
 * Created by tonya on 8/29/16.
 */
public class DayInfo implements Serializable{
    public String name;
    public String description;

    public DayInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
