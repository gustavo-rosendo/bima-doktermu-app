package com.bima.dokterpribadimu.model;

import java.util.List;

/**
 * Created by apradanas
 */
public class Partner {

    private int partnerId;
    private String partnerName;
    private String partnerAddress;
    private String partnerLong;
    private String partnerLat;
    private String partnerPhone;
    private String partnerPicture;
    private List<Discount> discount;

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerAddress() {
        return partnerAddress;
    }

    public void setPartnerAddress(String partnerAddress) {
        this.partnerAddress = partnerAddress;
    }

    public String getPartnerLong() {
        return partnerLong;
    }

    public void setPartnerLong(String partnerLong) {
        this.partnerLong = partnerLong;
    }

    public String getPartnerLat() {
        return partnerLat;
    }

    public void setPartnerLat(String partnerLat) {
        this.partnerLat = partnerLat;
    }

    public String getPartnerPhone() {
        return partnerPhone;
    }

    public void setPartnerPhone(String partnerPhone) {
        this.partnerPhone = partnerPhone;
    }

    public String getPartnerPicture() {
        return partnerPicture;
    }

    public void setPartnerPicture(String partnerPicture) {
        this.partnerPicture = partnerPicture;
    }

    public List<Discount> getDiscount() {
        return discount;
    }

    public void setDiscount(List<Discount> discount) {
        this.discount = discount;
    }
}
