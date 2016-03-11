package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by apradanas on 3/11/16.
 */
public interface BookingService {

    @FormUrlEncoded
    @POST("/developer/v1/booking")
    Call<BaseResponse> bookCall(
            @Field("call_topic") String callTopic,
            @Field("access_token") String accessToken
    );
}