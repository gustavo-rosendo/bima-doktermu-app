package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by apradanas on 3/12/16.
 */
public interface SubscriptionService {

    @FormUrlEncoded
    @POST("/v3/subscription")
    Call<BaseResponse> registerSubscription(
            @Field("subscription_type") String subscriptionType,
            @Field("subscription_lat") Double subscriptionLat,
            @Field("subscription_long") Double subscriptionLong,
            @Field("subscription_token") String subscriptionToken,
            @Field("subscription_start") String subscriptionStart,
            @Field("subscription_end") String subscriptionEnd,
            @Field("order_date") String orderDate,
            @Field("order_id") String orderId,
            @Field("payment_method") String paymentMethod,
            @Field("product_name") String productName,
            @Field("price") String price,
            @Field("date_of_purchase") String dateOfPurchase,
            @Field("policy_active_date") String policyActiveDate,
            @Field("policy_expiry_date") String policyExpiryDate,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/subscription/update")
    Call<BaseResponse> updateSubscription(
            @Field("order_id") String orderId,
            @Field("subscription_token") String subscriptionToken,
            @Field("product_name") String productName,
            @Field("price") String price,
            @Field("access_token") String accessToken
    );

    @GET("/v3/subscription/expired")
    Call<BaseResponse> setSubscriptionExpired(
            @Field("access_token") String accessToken
    );

    @GET("/v3/subscription/stop")
    Call<BaseResponse> setSubscriptionStopped(
            @Field("subscription_end") String subscriptionEnd,
            @Field("access_token") String accessToken
    );
}
