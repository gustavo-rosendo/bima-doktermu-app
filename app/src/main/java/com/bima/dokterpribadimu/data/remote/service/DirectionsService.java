package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.directions.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by apradanas.
 */
public interface DirectionsService {

    @GET("/maps/api/directions/json")
    Call<DirectionsResponse> getDistance(
            @Query("sensor") boolean sensor,
            @Query("origin") String origin,
            @Query("destination") String destination
    );
}
