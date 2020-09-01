package com.lohnguyen.aggiefeed.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feed_item_table")
public class FeedItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String displayName;
    private String objectType;
    private String published;
    private String json;

    @Nullable
    private String location;
    @Nullable
    private String startDate;
    @Nullable
    private String endDate;

    public FeedItem(String title, String displayName, String objectType, String published,
                    @Nullable String location, @Nullable String startDate, @Nullable String endDate, String json) {
        this.title = title;
        this.displayName = displayName;
        this.objectType = objectType;
        this.published = published;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getPublished() {
        return published;
    }

    public String getJson() {
        return json;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    @Nullable
    public String getStartDate() {
        return startDate;
    }

    @Nullable
    public String getEndDate() {
        return endDate;
    }
}
