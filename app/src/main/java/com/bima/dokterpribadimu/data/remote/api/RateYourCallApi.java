package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.RateYourCallService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gusta_000 on 13/5/2016.
 */
public class RateYourCallApi extends BaseApi<RateYourCallService> {

    private RateYourCallService rateYourCallService;

    public RateYourCallApi(){
        super(BaseApi.API_TYPE_SERVER);
        rateYourCallService = this.createService();
    }

    @Override
    protected Class getServiceClass(){
        return RateYourCallService.class;
    }

    /**
     * RateYourCallApi implementation to rate a call
     *
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> rateCall(
            final String callId, final Integer rating, final String accessToken){
        return Observable.create(new Observable.OnSubscribe<BaseResponse>(){
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber){
                try{
                    Response response = rateYourCallService.rateCall(callId, rating, accessToken).execute();
                    if(response.isSuccessful()){
                        subscriber.onNext((BaseResponse)response.body());
                        subscriber.onCompleted();
                    }else{
                        BaseResponse error= GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                }catch(IOException e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
