package com.bima.dokterpribadimu.model;

import java.util.List;

/**
 * Created by apradanas.
 */
public class HealthCondition {

    private String diabetes;
    private List<String> cancer;
    private String bloodPressure;
    private List<String> alergies;
    private String asthma;
    private String pregnant;

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public List<String> getCancer() {
        return cancer;
    }

    public void setCancer(List<String> cancer) {
        this.cancer = cancer;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public List<String> getAllergies() {
        return alergies;
    }

    public void setAllergies(List<String> allergies) {
        this.alergies = allergies;
    }

    public String getAsthma() {
        return asthma;
    }

    public void setAsthma(String asthma) {
        this.asthma = asthma;
    }

    public String getPregnant() {
        return pregnant;
    }

    public void setPregnant(String pregnant) {
        this.pregnant = pregnant;
    }
}
