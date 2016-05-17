package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.DirectionsService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.directions.DirectionsResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas.
 */
public class DirectionsApi extends BaseApi<DirectionsService> {

    private DirectionsService directionsService;

    public DirectionsApi() {
        super(BaseApi.API_TYPE_MAPS);
        directionsService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return DirectionsService.class;
    }

    /**
     * Directions implementation to get news
     * @return Observable<DirectionsResponse>
     */
    public Observable<DirectionsResponse> getDirections(final boolean sensor, final String origin, final String destination) {
        return Observable.create(new Observable.OnSubscribe<DirectionsResponse>() {
            @Override
            public void call(final Subscriber<? super DirectionsResponse> subscriber) {
                try {
                    Response response = directionsService.getDistance(sensor, origin, destination).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((DirectionsResponse) response.body());
                        subscriber.onCompleted();
                    } else {
                        BaseResponse error = GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
