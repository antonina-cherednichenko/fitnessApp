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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ReceipeInfo that = (ReceipeInfo) o;
//
//        if (photoId != that.photoId) return false;
//        if (!name.equals(that.name)) return false;
//        return description.equals(that.description);
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = name.hashCode();
//        result = 31 * result + description.hashCode();
//        result = 31 * result + photoId;
//        return result;
//    }
}
