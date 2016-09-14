package com.bima.dokterpribadimu.model;

/**
 * Created by apradanas on 3/12/16.
 */
public class Subscription {

    private String subscriptionType;
    private Double subscriptionLat;
    private Double subscriptionLong;
    private String subscriptionToken;
    private String subscriptionStart;
    private String subscriptionEnd;
    private String name;
    private String orderDate;
    private String orderId;
    private String paymentMethod;
    private String phoneNumber;
    private String productName;
    private String price;
    private String dateOfBirth;
    private String gender;
    private String dateOfPurchase;
    private String policyActiveDate;
    private String policyExpiryDate;
    private String policy;
    private String accessToken;

    public Subscription() {

    }

    public Subscription(
            String subscriptionType, Double subscriptionLat, Double subscriptionLong,
            String subscriptionToken, String subscriptionStart, String subscriptionEnd,
            String name, String orderDate, String orderId,
            String paymentMethod, String phoneNumber, String productName,
            String price, String dateOfBirth, String gender, String dateOfPurchase,
            String policyActiveDate, String policyExpiryDate, String policy, String accessToken) {

        this.subscriptionType = subscriptionType;
        this.subscriptionLat = subscriptionLat;
        this.subscriptionLong = subscriptionLong;
        this.subscriptionToken = subscriptionToken;
        this.subscriptionStart = subscriptionStart;
        this.subscriptionEnd = subscriptionEnd;
        this.name = name;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
        this.productName = productName;
        this.price = price;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.dateOfPurchase = dateOfPurchase;
        this.policyActiveDate = policyActiveDate;
        this.policyExpiryDate = policyExpiryDate;
        this.policy = policy;
        this.accessToken = accessToken;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
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

    public String getSubscriptionStart() {
        return subscriptionStart;
    }

    public void setSubscriptionStart(String subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public String getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public void setSubscriptionEnd(String subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfPurchase() { return dateOfPurchase; }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getPolicyActiveDate() { return policyActiveDate; }

    public void setPolicyActiveDate(String policyActiveDate) {
        this.policyActiveDate = policyActiveDate;
    }

    public String getPolicyExpiryDate() { return policyExpiryDate; }

    public void setPolicyExpiryDate(String policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
