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
    private String subscriptionStatus;
    private String name;
    private String orderDate;
    private String orderId;
    private String paymentMethod;
    private String phoneNumber;
    private String productName;
    private String type;
    private String price;
    private String accessToken;

    public Subscription(
            String subscriptionType, Double subscriptionLat, Double subscriptionLong,
            String subscriptionToken, String subscriptionStart, String subscriptionEnd,
            String subscriptionStatus, String name, String orderDate, String orderId,
            String paymentMethod, String phoneNumber, String productName, String type,
            String price, String accessToken) {
        this.subscriptionType = subscriptionType;
        this.subscriptionLat = subscriptionLat;
        this.subscriptionLong = subscriptionLong;
        this.subscriptionToken = subscriptionToken;
        this.subscriptionStart = subscriptionStart;
        this.subscriptionEnd = subscriptionEnd;
        this.subscriptionStatus = subscriptionStatus;
        this.name = name;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
        this.productName = productName;
        this.type = type;
        this.price = price;
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

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
