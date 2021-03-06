package com.bima.dokterpribadimu.model;

/**
 * Created by apradanas on 2/11/16.
 */
public class UserProfile {

    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String picture;
    private String msisdn;
    private String dateOfBirth;
    private String gender;
    private String referral;
    private String loginType;
    private Double registerLat;
    private Double registerLong;
    private String deviceType;
    private String deviceImei;
    private String deviceSoftware;
    private String deviceOperator;
    private final String product    = "DS"; //Always the same, according to Connor (BIMA)
    private final String policy     = "App Basic"; //Always the same, according to Connor (BIMA)
    private String accessToken;

    public UserProfile(
            String id, String name, String firstName, String lastName,
            String email, String picture, String msisdn, String referral,
            String loginType, Double registerLat, Double registerLong, String accessToken) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
        this.msisdn = msisdn;
        this.referral = referral;
        this.loginType = loginType;
        this.registerLat = registerLat;
        this.registerLong = registerLong;
        this.accessToken = accessToken;
    }

    // constructor with device info
    public UserProfile(
            String id, String name, String firstName, String lastName, String email,
            String picture, String msisdn, String referral, String loginType,
            Double registerLat, Double registerLong, String deviceType, String deviceImei,
            String deviceSoftware, String deviceOperator, String accessToken) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
        this.msisdn = msisdn;
        this.referral = referral;
        this.loginType = loginType;
        this.registerLat = registerLat;
        this.registerLong = registerLong;
        this.deviceType = deviceType;
        this.deviceImei = deviceImei;
        this.deviceSoftware = deviceSoftware;
        this.deviceOperator = deviceOperator;
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Double getRegisterLat() {
        return registerLat;
    }

    public void setRegisterLat(Double registerLat) {
        this.registerLat = registerLat;
    }

    public Double getRegisterLong() {
        return registerLong;
    }

    public void setRegisterLong(Double registerLong) {
        this.registerLong = registerLong;
    }

    public String getProduct() {
        return product;
    }

    public String getPolicy() {
        return policy;
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
