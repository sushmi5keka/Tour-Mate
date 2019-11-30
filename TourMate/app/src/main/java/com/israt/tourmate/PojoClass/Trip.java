package com.israt.tourmate.PojoClass;

public class Trip {
    private String tripId,name,location,budget,startD,endD;

    public Trip() {
    }

    public Trip(String tripId, String name, String location, String budget, String startD, String endD) {
        this.tripId = tripId;
        this.name = name;
        this.location = location;
        this.budget = budget;
        this.startD = startD;
        this.endD = endD;
    }

    public String getTripId() {
        return tripId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getBudget() {
        return budget;
    }

    public String getStartD() {
        return startD;
    }

    public String getEndD() {
        return endD;
    }
}
