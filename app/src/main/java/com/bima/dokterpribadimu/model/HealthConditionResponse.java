package com.bima.dokterpribadimu.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apradanas.
 */
public class HealthConditionResponse {

    @SerializedName("HealthCondition")
    private HealthCondition healthCondition;

    public HealthCondition getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(HealthCondition healthCondition) {
        this.healthCondition = healthCondition;
    }
}
