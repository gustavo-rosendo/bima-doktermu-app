package com.bima.dokterpribadimu.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apradanas.
 */
public class MedicineInfoResponse {

    @SerializedName("MedicineInfo")
    private MedicineInfo medicineInfo;

    public MedicineInfo getMedicineInfo() {
        return medicineInfo;
    }

    public void setMedicineInfo(MedicineInfo medicineInfo) {
        this.medicineInfo = medicineInfo;
    }
}
