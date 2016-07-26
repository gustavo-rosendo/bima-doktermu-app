package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CallHistoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gusta_000 on 16/5/2016.
 */
public interface CallHistoryService {

    @GET("/v3/booking/history")
    Call<BaseResponse<CallHistoryResponse>> getCallHistory(
            @Query("limit") int limit,
            @Query("access_token") String accessToken
    );

}
