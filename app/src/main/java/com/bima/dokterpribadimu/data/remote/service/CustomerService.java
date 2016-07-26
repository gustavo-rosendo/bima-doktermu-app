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
    @POST("/v3/customer")
    Call<BaseResponse> registerCustomer(
            @Field("name") String name,
            @Field("phone_number") String phoneNumber,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("access_token") String accessToken
    );
}
