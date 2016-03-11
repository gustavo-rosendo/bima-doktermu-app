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
    @POST("/developer/v1/subscription")
    Call<BaseResponse> registerSubscription(
            @Field("subscription_type") String subscriptionType,
            @Field("subscription_lat") Double subscriptionLat,
            @Field("subscription_long") Double subscriptionLong,
            @Field("subscription_token") String subscriptionToken,
            @Field("subscription_start") String subscriptionStart,
            @Field("subscription_end") String subscriptionEnd,
            @Field("subscription_status") String subscriptionStatus,
            @Field("name") String name,
            @Field("order_date") String orderDate,
            @Field("order_id") String orderId,
            @Field("payment_method") String paymentMethod,
            @Field("phone_number") String phoneNumber,
            @Field("product_name") String productName,
            @Field("type") String type,
            @Field("price") String price,
            @Field("access_token") String accessToken
    );

    @GET("/developer/v1/subscription/expired")
    Call<BaseResponse> setSubscriptionExpired(
            @Field("access_token") String accessToken
    );

    @GET("/developer/v1/subscription/stop")
    Call<BaseResponse> setSubscriptionStopped(
            @Field("subscription_end") String subscriptionEnd,
            @Field("access_token") String accessToken
    );
}
