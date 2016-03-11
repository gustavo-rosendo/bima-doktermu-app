package com.bima.dokterpribadimu.model;

/**
 * Created by apradanas on 3/12/16.
 */
public class Customer {

    private String name;
    private String dob;
    private String msisdn;
    private String product;
    private String subscriptionType;
    private String dateOfPurchase;
    private String policyActiveDate;
    private String policyExpiryDate;
    private String email;
    private String gender;
    private Double subscriptionLat;
    private Double subscriptionLong;
    private String subscriptionToken;
    private String deviceType;
    private String deviceImei;
    private String deviceSoftware;
    private String deviceOperator;
    private String accessToken;

    public Customer(
            String name, String dob, String msisdn, String product, String subscriptionType,
            String dateOfPurchase, String policyActiveDate, String policyExpiryDate, String email,
            String gender, Double subscriptionLat, Double subscriptionLong, String subscriptionToken,
            String deviceType, String deviceImei, String deviceSoftware, String deviceOperator, String accessToken) {
        this.name = name;
        this.dob = dob;
        this.msisdn = msisdn;
        this.product = product;
        this.subscriptionType = subscriptionType;
        this.dateOfPurchase = dateOfPurchase;
        this.policyActiveDate = policyActiveDate;
        this.policyExpiryDate = policyExpiryDate;
        this.email = email;
        this.gender = gender;
        this.subscriptionLat = subscriptionLat;
        this.subscriptionLong = subscriptionLong;
        this.subscriptionToken = subscriptionToken;
        this.deviceType = deviceType;
        this.deviceImei = deviceImei;
        this.deviceSoftware = deviceSoftware;
        this.deviceOperator = deviceOperator;
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getPolicyActiveDate() {
        return policyActiveDate;
    }

    public void setPolicyActiveDate(String policyActiveDate) {
        this.policyActiveDate = policyActiveDate;
    }

    public String getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    public void setPolicyExpiryDate(String policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getSubscriptionLat() {
        return subscriptionLat;
    }

    public void setSubscriptionLat(Double subscriptionLat) {
        this.subscriptionLat = subscriptionLat;
    }

    public Double getSubscriptionLong() {
        return subscriptionLong;
    }

    public void setSubscriptionLong(Double subscriptionLong) {
        this.subscriptionLong = subscriptionLong;
    }

    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    public void setSubscriptionToken(String subscriptionToken) {
        this.subscriptionToken = subscriptionToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getDeviceSoftware() {
        return deviceSoftware;
    }

    public void setDeviceSoftware(String deviceSoftware) {
        this.deviceSoftware = deviceSoftware;
    }

    public String getDeviceOperator() {
        return deviceOperator;
    }

    public void setDeviceOperator(String deviceOperator) {
        this.deviceOperator = deviceOperator;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
