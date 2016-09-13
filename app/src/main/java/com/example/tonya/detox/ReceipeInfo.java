package com.example.tonya.detox;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class ReceipeInfo implements Serializable {
    public String description;
    public int photoId;
    public boolean liked;
    public List<DayInfo> days;
    public String name;
    public int duration;
    public String shortDescription;

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDays(List<DayInfo> days) {
        this.days = days;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceipeInfo that = (ReceipeInfo) o;

        if (photoId != that.photoId) return false;
        if (!name.equals(that.name)) return false;
        return description.equals(that.description);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + photoId;
        return result;
    }
}
