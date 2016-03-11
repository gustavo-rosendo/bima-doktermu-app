package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by apradanas on 3/12/16.
 */
public interface CustomerService {

    @FormUrlEncoded
    @POST("/developer/v1/customer")
    Call<BaseResponse> registerCustomer(
            @Field("name") String name,
            @Field("dob") String dob,
            @Field("msisdn") String msisdn,
            @Field("product") String product,
            @Field("subscription_type") String subscriptionType,
            @Field("date_of_purchase") String dateOfPurchase,
            @Field("policy_active_date") String policyActiveDate,
            @Field("policy_expiry_date") String policyExpiryDate,
            @Field("email") String email,
            @Field("gender") String gender,
            @Field("subscription_lat") Double subscriptionLat,
            @Field("subscription_long") Double subscriptionLong,
            @Field("subscription_token") String subscriptionToken,
            @Field("device_type") String deviceType,
            @Field("device_imei") String deviceImei,
            @Field("device_software") String deviceSoftware,
            @Field("device_operator") String deviceOperator,
            @Field("access_token") String accessToken
    );
}
