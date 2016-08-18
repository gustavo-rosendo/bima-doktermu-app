package com.bima.dokterpribadimu.model;

/**
 * Created by apradanas.
 */
public class Prescription {

    private String date;
    private String name;
    private String duration;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
