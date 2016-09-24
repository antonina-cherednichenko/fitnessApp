package com.cherednichenko.antonina.detoxdiet.detox_diet_data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class ProgramInfo implements Serializable {
    private String description;
    private int photoId;
    private int liked;
    private List<DayInfo> days;
    private String name;
    private int duration;
    private String shortDescription;

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

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public List<DayInfo> getDays() {
        return days;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public int getLiked() {
        return liked;
    }

    public String getName() {
        return name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgramInfo that = (ProgramInfo) o;

        if (photoId != that.photoId) return false;
        if (!name.equals(that.name)) return false;
        return description.equals(that.description);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
