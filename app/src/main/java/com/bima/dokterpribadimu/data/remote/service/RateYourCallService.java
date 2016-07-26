package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by gusta_000 on 13/5/2016.
 */
public interface RateYourCallService {

    @FormUrlEncoded
    @POST("/v3/booking/rate")
    Call<BaseResponse> rateCall(
            @Field("call_id") String callId,
            @Field("rating") Integer rating,
            @Field("access_token") String accessToken
    );
}
