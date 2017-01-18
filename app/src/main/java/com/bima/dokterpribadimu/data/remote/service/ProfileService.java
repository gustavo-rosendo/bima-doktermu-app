package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.HealthConditionResponse;
import com.bima.dokterpribadimu.model.InformationResponse;
import com.bima.dokterpribadimu.model.MedicineInfoResponse;
import com.bima.dokterpribadimu.model.ProfileResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by gustavo.santos on 5/16/2016.
 */
public interface ProfileService {

    @GET("/v3/profile")
    Call<BaseResponse<ProfileResponse>> getProfileInfo(
            @Query("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/profile/phone_number")
    Call<BaseResponse> changePhoneNumber(
            @Field("phone_number") String newPhoneNumber,
            @Field("access_token") String accessToken
    );

    @GET("/v3/profile/info")
    Call<BaseResponse<InformationResponse>> getHealthInformation(
            @Query("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/profile/info")
    Call<BaseResponse> updateHealthInformation(
            @Field("height") String height,
            @Field("weight") String weight,
            @Field("religion") String religion,
            @Field("blood_type") String bloodType,
            @Field("smoker") String smoker,
            @Field("physical_exercise") String physicalExercise,
            @Field("health_insurance") String healthInsurance,
            @Field("access_token") String accessToken
    );

    @GET("/v3/profile/health")
    Call<BaseResponse<HealthConditionResponse>> getHealthCondition(
            @Query("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/profile/health")
    Call<BaseResponse> updateHealthConditionSingleValue(
            @Field("diabetes") String diabetes,
            @Field("cancer") ArrayList<String> cancer,
            @Field("blood_pressure") String bloodPressure,
            @Field("alergies") ArrayList<String> allergies,
            @Field("asthma") String asthma,
            @Field("pregnant") String pregnant,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/profile/health")
    Call<BaseResponse> updateHealthConditionSingleAllergy(
            @Field("diabetes") String diabetes,
            @Field("cancer[]") ArrayList<String> cancer,
            @Field("blood_pressure") String bloodPressure,
            @Field("alergies") ArrayList<String> allergies,
            @Field("asthma") String asthma,
            @Field("pregnant") String pregnant,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/profile/health")
    Call<BaseResponse> updateHealthConditionSingleCancer(
            @Field("diabetes") String diabetes,
            @Field("cancer") ArrayList<String> cancer,
            @Field("blood_pressure") String bloodPressure,
            @Field("alergies[]") ArrayList<String> allergies,
            @Field("asthma") String asthma,
            @Field("pregnant") String pregnant,
            @Field("access_token") String accessToken
    );

    @FormUrlEncoded
    @POST("/v3/profile/health")
    Call<BaseResponse> updateHealthCondition(
            @Field("diabetes") String diabetes,
            @Field("cancer[]") ArrayList<String> cancer,
            @Field("blood_pressure") String bloodPressure,
            @Field("alergies[]") ArrayList<String> allergies,
            @Field("asthma") String asthma,
            @Field("pregnant") String pregnant,
            @Field("access_token") String accessToken
    );

    @GET("/v3/profile/medicine")
    Call<BaseResponse<MedicineInfoResponse>> getMedicineInformation(
            @Query("access_token") String accessToken
    );
}
