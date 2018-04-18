package com.example.android.medmanagerapplication.drugs;

public class Drugs {
    public long drugId;
    public String name;
    public String description;
    public int interval;
    public long startDate;
    public long endDate;
    public long duration;

    public Drugs(long durgId,
                 String name,
                 String description,
                 int interval,
                 long startDate,
                 long endDate,
                 long duration) {
        this.drugId = durgId;
        this.name = name;
        this.description = description;
        this.interval = interval;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public long getDrugId() {
        return drugId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getInterval() {
        return interval;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getDuration() {
        return duration;
    }
}

