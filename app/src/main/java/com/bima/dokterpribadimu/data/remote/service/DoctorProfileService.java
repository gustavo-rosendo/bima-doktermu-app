package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.DoctorProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gustavo.santos on 8/23/2016.
 */
public interface DoctorProfileService {

    @GET("/v3/doctor/profile")
    Call<BaseResponse<DoctorProfileResponse>> getDoctorProfile(
            @Query("doctor_id") String doctorId,
            @Query("access_token") String accessToken
    );

}
