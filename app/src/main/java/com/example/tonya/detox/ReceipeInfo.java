package com.example.tonya.detox;

import java.io.Serializable;

/**
 * Created by tonya on 8/29/16.
 */
public class ReceipeInfo implements Serializable {
    public String name;
    public String description;
    public int photoId;
    public boolean liked;

    public ReceipeInfo(String name, String description, int photoId, boolean liked) {
        this.name = name;
        this.description = description;
        this.photoId = photoId;
        this.liked = liked;
    }
}
