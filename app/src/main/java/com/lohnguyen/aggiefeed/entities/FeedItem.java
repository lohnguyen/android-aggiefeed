package com.lohnguyen.aggiefeed.entities;

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
    private String location;
    private String startDate;
    private String endDate;
    private String json;

    public FeedItem(String title, String displayName, String objectType, String published,
                    String location, String startDate, String endDate, String json) {
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

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getJson() {
        return json;
    }
}
