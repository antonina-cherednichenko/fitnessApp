package com.example.tonya.detox;

/**
 * Created by tonya on 8/29/16.
 */
public class ReceipeInfo {
    public String name;
    public String description;
    public int photoId;
    public int liked;

    public ReceipeInfo(String name, String description, int photoId, int liked) {
        this.name = name;
        this.description = description;
        this.photoId = photoId;
        this.liked = liked;
    }
}
