package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by apradanas on 3/5/16.
 */
public interface UserService {

    @FormUrlEncoded
    @POST("/v2/user/login")
    Call<BaseResponse> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("login_type") String loginType,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("name") String name,
            @Field("picture") String picture,
            @Field("msisdn") String msisdn,
            @Field("referral") String referral,
            @Field("registration_lat") Double registrationLat,
            @Field("registration_long") Double registrationLong,
            @Field("device_type") String deviceType,
            @Field("device_imei") String deviceImei,
            @Field("device_software") String deviceSoftware,
            @Field("device_operator") String deviceOperator,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v2/user/register")
    Call<BaseResponse> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("login_type") String loginType,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("name") String name,
            @Field("picture") String picture,
            @Field("msisdn") String msisdn,
            @Field("referral") String referral,
            @Field("registration_lat") Double registrationLat,
            @Field("registration_long") Double registrationLong,
            @Field("device_type") String deviceType,
            @Field("device_imei") String deviceImei,
            @Field("device_software") String deviceSoftware,
            @Field("device_operator") String deviceOperator,
            @Field("product") String product,
            @Field("policy") String policy,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v2/user/update")
    Call<BaseResponse> update(
            @Field("name") String name,
            @Field("phone_number") String msisdn,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v2/user/password")
    Call<BaseResponse> changePassword(
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword,
            @Field("access_token") String accessToken
    );
}
