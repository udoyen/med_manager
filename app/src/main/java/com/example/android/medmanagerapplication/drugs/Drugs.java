package com.example.android.medmanagerapplication.drugs;

public class Drugs {
    public long drugId;
    public String name;
    public String description;
    public int interval;
    public String startDate;
    public String endDate;
    public long duration;

    public Drugs (long durgId, String name, String description, int interval, String startDate, String endDate, long duration) {
        this.drugId = durgId;
        this.name = name;
        this.description = description;
        this.interval = interval;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }
}

