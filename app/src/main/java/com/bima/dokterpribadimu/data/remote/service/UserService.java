package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by apradanas on 3/5/16.
 */
public interface UserService {

    @FormUrlEncoded
    @POST("/developer/v1/user/login")
    Call<BaseResponse> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("login_type") String loginType,
            @Field("access_token") String accessToken,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("name") String name,
            @Field("picture") String picture,
            @Field("msisdn") String msisdn,
            @Field("referral") String referral,
            @Field("registration_lat") Double registrationLat,
            @Field("registration_long") Double registrationLong
    );

    @FormUrlEncoded
    @POST("/developer/v1/user/register")
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
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/developer/v1/user/password")
    Call<BaseResponse> changePassword(
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword,
            @Field("access_token") String accessToken
    );
}
