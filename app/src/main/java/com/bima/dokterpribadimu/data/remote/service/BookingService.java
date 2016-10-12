package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CallAssignedResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by apradanas on 3/11/16.
 */
public interface BookingService {

    @FormUrlEncoded
    @POST("/v3/booking")
    Call<BaseResponse> bookCall(
            @Field("call_topic") String callTopic,
            @Field("call_subtopic") String callSubTopic,
            @Field("user_notes") CharSequence userNotes,
            @Field("phone_number") String phoneNumber,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/booking/cancel")
    Call<BaseResponse> cancelCall(
            @Field("call_id") String callId,
            @Field("access_token") String accessToken
    );

    @GET("/v3/booking/assignment")
    Call<BaseResponse<CallAssignedResponse>> getCallAssignment(
            @Query("call_id") String callId,
            @Query("access_token") String accessToken
    );
}
