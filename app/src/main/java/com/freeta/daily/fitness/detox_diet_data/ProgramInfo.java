package com.freeta.daily.fitness.detox_diet_data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class ProgramInfo implements Serializable {
    private String description;
    private String photoURL;
    private int liked;
    private int isNew;
    private int recommended;
    private List<DayInfo> days;
    private String name;
    private int duration;
    private String shortDescription;
    private String category;
    private String fromSourceName;
    private String fromSourceUrl;

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getRecommended() {
        return recommended;
    }

    public void setRecommended(int recommended) {
        this.recommended = recommended;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

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

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFromSourceName() {
        return fromSourceName;
    }

    public void setFromSourceName(String fromSourceName) {
        this.fromSourceName = fromSourceName;
    }

    public String getFromSourceUrl() {
        return fromSourceUrl;
    }

    public void setFromSourceUrl(String fromSourceUrl) {
        this.fromSourceUrl = fromSourceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgramInfo that = (ProgramInfo) o;

        if (!photoURL.equals(photoURL)) return false;
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
