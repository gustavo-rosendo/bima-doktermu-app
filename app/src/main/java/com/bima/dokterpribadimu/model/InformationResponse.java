package com.bima.dokterpribadimu.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apradanas.
 */
public class InformationResponse {

    @SerializedName("Information")
    private Information information;

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}
