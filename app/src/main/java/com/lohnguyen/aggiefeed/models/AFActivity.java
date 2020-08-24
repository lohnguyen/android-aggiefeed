package com.lohnguyen.aggiefeed.models;

public class AFActivity {
    public String title;
    public String displayName;
    public String objectType;
    public String published;
    public String location;
    public String startDate;
    public String endDate;

    public AFActivity(String title, String displayName, String objectType, String published,
                      String location, String startDate, String endDate) {
        this.title = title;
        this.displayName = displayName;
        this.objectType = objectType;
        this.published = published;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
