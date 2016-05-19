package com.bima.dokterpribadimu.model;

/**
 * Created by gustavo.santos on 5/19/2016.
 */
public class Profile {
    private String subscriptionStart;
    private String subscriptionEnd;
    private String policyActiveDate;
    private String policyExpiryDate;
    private String memberNumber;
    private String accessToken;

    public String getSubscriptionStart() {
        return subscriptionStart;
    }

    public String getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public String getPolicyActiveDate() {
        return policyActiveDate;
    }

    public String getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setSubscriptionStart(String subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public void setSubscriptionEnd(String subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

    public void setPolicyActiveDate(String policyActiveDate) {
        this.policyActiveDate = policyActiveDate;
    }

    public void setPolicyExpiryDate(String policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
